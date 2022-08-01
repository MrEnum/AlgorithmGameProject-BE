package com.seventeam.algoritmgameproject.web.service.compiler_service;

import com.seventeam.algoritmgameproject.domain.model.questions.TestCase;
import com.seventeam.algoritmgameproject.domain.model.login.User;
import com.seventeam.algoritmgameproject.web.dto.questions_dto.QuestionRedis;
import com.seventeam.algoritmgameproject.web.dto.questions_dto.TestCaseRedis;
import com.seventeam.algoritmgameproject.web.repository.game_repository.QuestionsRedisRepository;
import com.seventeam.algoritmgameproject.web.repository.game_repository.TestCaseRedisRepository;
import com.seventeam.algoritmgameproject.web.repository.questions_repository.TestCaseDslRepository;
import com.seventeam.algoritmgameproject.web.dto.compiler_dto.CompileRequestDto;
import com.seventeam.algoritmgameproject.web.dto.compiler_dto.CompileResultDto;
import com.seventeam.algoritmgameproject.web.service.compiler_service.generatedTemplate.GeneratedTemplate;
import com.seventeam.algoritmgameproject.web.service.game_service.GameProcess;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Service
@RequiredArgsConstructor
public class CompilerServiceImp implements CompilerService {
    private final Map<String, GeneratedTemplate> templateMap;
    private final TestCaseDslRepository repository;
    private final JDoodleApi jDoodleApi;
    private final GameProcess service;
    private final QuestionsRedisRepository questionsRedisRepository;
    private final TestCaseRedisRepository testCaseRedisRepository;
    private static final Map<String, Boolean> compileRequestMap = new ConcurrentHashMap<>();

    @Override
    public CompileResultDto compileResult(CompileRequestDto dto, User user) {
        if (!compileRequestMap.containsKey(dto.getRoomId())) {
            compileRequestMap.put(dto.getRoomId(), true);
        } else {
            return CompileResultDto.builder()
                    .msg("상대 사용자가 컴파일 중입니다.")
                    .result(false)
                    .build();
        }

        GeneratedTemplate template = null;
        Language language = null;
        switch (dto.getLanguageIdx()) {
            case 0: {
                template = templateMap.get("generatedJavaTemplateImp");
                language = Language.JAVA;
                break;
            }
            case 1: {
                template = templateMap.get("generatedJavascriptTemplateImp");
                language = Language.NODEJS;
                break;
            }
            case 2: {
                template = templateMap.get("generatedPython3TemplateImp");
                language = Language.PYTHON3;
                break;
            }
        }

        //List<TestCase> testCases = repository.getTestCases(dto.getQuestionId());

        if (template == null) {
            throw new RuntimeException("해당 언어는 지원하지 않습니다");
        }

        List<TestCaseRedis> testCases = getTestCases(dto.getQuestionId());
        log.info("테스트 케이스 목록:{}",testCases.toString());
        JSONObject compile = Optional
                .ofNullable(jDoodleApi.compile(template.compileCode(dto.getCodeStr(), testCases, dto.getQuestionId()), language))
                .orElseThrow(() -> new RuntimeException("컴파일 실패"));

        String output = compile.get("output").toString();

        if (output.contains("error") || output.contains("ReferenceError") || output.contains("Traceback")) {

            return errorResult(output, dto, user);

        } else if (output.contains("No \"public class\" found to execute")) {

            return publicClassErrorResult(dto, user);

        } else {

            return compileCheckResult(output, testCases, language, dto, user);
        }
    }

    @Override
    public CompileResultDto compileCheckResult(String output, List<TestCaseRedis> testCases, Language language, CompileRequestDto dto, User user) {
        String[] ans = output.replace("\n", "").split("_");
        int cnt = 0;
        boolean resultBool = true;
        String msg;

        //테스트케이스의 개수로 퍼센트지 계산을 위한 변수
        int divide = (100 / testCases.size());
        for (int i = 0; i < ans.length; i++) {
            String answer = testCases.get(i).getAnswer().replaceAll("\\p{Z}", "");
            StringBuilder result = new StringBuilder(ans[i].replaceAll("\\p{Z}", ""));

            if (0 < answer.indexOf("[") && !language.equals(Language.JAVA)) {
                result.insert(0, "[");
                result.append("]");
            }

            if (answer.equals(result.toString())) {
                cnt += divide;
            }
        }
        if (cnt < 100) {
            resultBool = false;
            msg = "오답";
            service.compileFail(user.getUserId(), dto.getRoomId());
        } else {
            service.winAndOtherLoseProcess(dto.getRoomId(), user);
            msg = "승리";
        }
        //컴파일 동시성 문제 방지
        compileRequestMap.remove(dto.getRoomId());
        return CompileResultDto.builder()
                .msg(msg)
                .result(resultBool)
                .build();

    }

    @Override
    public CompileResultDto errorResult(String output, CompileRequestDto dto, User user) {

        int len = output.length();
        StringBuilder out = new StringBuilder(output);
        out.deleteCharAt(0);
        out.deleteCharAt(len - 2);
        output = out.toString();

        String[] split = output.split("\n");
        StringBuilder errorMsg = new StringBuilder(50);

        for (String s : split) {
            s = s
                    .replace(" ", "&#32;")
                    .replace("/", "&#47;")
                    .replace("^", "");

            errorMsg.append(s.trim());
            errorMsg.append("<br>");
        }

        compileRequestMap.remove(dto.getRoomId());
        service.compileFail(user.getUserId(), dto.getRoomId());

        return CompileResultDto.builder()
                .msg(errorMsg.toString())
                .result(false)
                .build();
    }

    @Override
    public CompileResultDto publicClassErrorResult(CompileRequestDto dto, User user) {
        compileRequestMap.remove(dto.getRoomId());
        service.compileFail(user.getUserId(), dto.getRoomId());
        return CompileResultDto.builder()
                .msg("No \"public class\" found to execute")
                .result(false)
                .build();
    }

    private List<TestCaseRedis>getTestCases(Long questionId){
        log.info("테스트 케이스 탐색");
        try {
            //기본
            QuestionRedis byId = questionsRedisRepository.findById(questionId).orElseThrow(() -> new NullPointerException("데이터 없음"));
            Iterable<TestCaseRedis> allById = testCaseRedisRepository.findAllById(byId.getCasesIds());
            List<TestCaseRedis> testcases = new ArrayList<>();
            allById.forEach(testcases::add);
            return testcases;

        }catch (NullPointerException e){
            // 익셉션 시 DB 데이터 가져오기
            List<TestCase> list = repository.getTestCases(questionId);
            List<TestCaseRedis> cases = new ArrayList<>();

            for (TestCase testCase : list) {
                TestCaseRedis redis = new TestCaseRedis();
                BeanUtils.copyProperties(testCase,redis);
                redis.setQuestionId(questionId);
                cases.add(redis);
            }
            return cases;
        }
    }
}
