package com.seventeam.algoritmgameproject.web.service.compiler_service.generatedTemplate;

import com.seventeam.algoritmgameproject.domain.model.questions.TestCase;
import com.seventeam.algoritmgameproject.web.dto.questions_dto.TestCaseRedis;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/*자바 문법에 맞는 출력문 문자열 추가 클래스*/
@Slf4j
@Component
@RequiredArgsConstructor
public class GeneratedJavaTemplateImp implements GeneratedTemplate {
    private final RedisTemplate<String,String> redisTemplate;
    @Override
    @Transactional
    public String compileCode(String codeStr, List<TestCaseRedis> testCases, Long questionId) {

        if(codeStr.contains("main")){
            throw new IllegalArgumentException("출력문을 작성하지 마세요!");
        }
        //클래스명 변경
        if (!codeStr.contains("Solution")) {
            int start = codeStr.indexOf("s");
            int end = codeStr.indexOf("{");
            String methodName = codeStr.substring(start + 2, end).trim();
            codeStr = codeStr.replace(methodName, "Solution");
        }
        int i = codeStr.lastIndexOf('}');

        StringBuilder buffer = new StringBuilder(codeStr).deleteCharAt(i);
        String ansType = testCases.get(0).getAnsType();


        if ((ansType != null && ansType.contains("[]")) && !codeStr.contains("java.util.Arrays;")) {
            buffer.insert(0, "import java.util.Arrays;");
        }

        if(Boolean.TRUE.equals(redisTemplate.hasKey("javaTemplate" + questionId))){
            buffer.append(redisTemplate.opsForValue().get("javaTemplate"+questionId));
        }else{
            redisTemplate.opsForValue().set("javaTemplate"+questionId, addTestCode(testCases));
            buffer.append(addTestCode(testCases));
        }
        return buffer.toString();
    }

    @Override
    public String addTestCode(List<TestCaseRedis> testCases) {
        StringBuilder buffer = new StringBuilder(50);

        buffer.append("public static void main(String[] args) {");
        buffer.append("Solution s = new Solution();");

        generatedTemplate(buffer, testCases);

        buffer.append("}}");
        return buffer.toString();
    }

    @Override
    public void generatedTemplate(StringBuilder variable, List<TestCaseRedis> testCases) {
        String typeStr = testCases.get(0).getType();
        //같은 인자가 복수 개일 때
        if (typeStr.contains("_")) {
            generatedAboutSameMultiParam(variable, testCases);
        } else {
            //인자가 하나일 때, 서로 다른 타입은 추후 다른 문제 추가 시, 업데이트
            if (!typeStr.contains(",")) {
                generatedAboutSingleParam(variable, testCases);
            }
        }
    }

    @Override
    public void generatedAboutSingleParam(StringBuilder variable, List<TestCaseRedis> testCases) {

        int initVarName = 97;
        int num = 0;
        String ansTypeStr = testCases.get(0).getAnsType();

        for (TestCaseRedis testCase : testCases) {
            List<String> varList = new ArrayList<>();
            String params = testCase.getParams();
            String typeStr = testCase.getType();


            if (typeStr.contains("[]")) {
                String param = params.replaceAll("[\\[\\]]", "");
                variable.append(typeStr).append(" ").append((char) (initVarName)).append(num).append("={").append(param).append("};");
            } else if (typeStr.equals("String")) {
                variable.append(typeStr).append(" ").append((char) (initVarName)).append(num).append("=\"").append(params).append("\";");
            } else {
                variable.append(typeStr).append(" ").append((char) (initVarName)).append(num).append("=").append(params).append(";");
            }
            if (initVarName == 122) {
                initVarName = 97;
            }
            //변수 생성 및 초기화
            varList.add((char) (initVarName) + String.valueOf(num));
            initVarName++;
            num++;
            //출력문 생성
            generatedOut(variable, varList, ansTypeStr);
        }

    }

    @Override
    public void generatedAboutSameMultiParam(StringBuilder variable, List<TestCaseRedis> testCases) {
        int initVarName = 97;
        int num = 0;
        String ansTypeStr = testCases.get(0).getAnsType();

        for (TestCaseRedis testCase : testCases) {
            List<String> varList = new ArrayList<>();
            String params = testCase.getParams();
            String typeStr = testCase.getType();

            String[] tmp = typeStr.split("_");
            List<String> paramList = new ArrayList<>();
            String type = tmp[0];
            int cnt = Integer.parseInt(tmp[1]);

            //배열 확인
            if (type.contains("[]")) {
                String[] ans = params.trim().split("_");
                for (String an : ans) {
                    StringBuilder parameter = new StringBuilder(40);
                    String param = an.replaceAll("[\\[\\]]", "");
                    parameter.append("{").append(param).append("}");
                    paramList.add(parameter.toString());
                }
            } else {
                //같은 복수개의 원시 타입 인자를 가지고 있을 때
                String[] ans = params.trim().split(",");
                Collections.addAll(paramList, ans);
            }
            //자바 파이썬에 맞게 수정
            //변수 생성 및 변수 정의 및 초기화 코드
            for (int i = 0; i < cnt; i++) {
                if (initVarName == 122) {
                    initVarName = 97;
                } else if (i == 0) {
                    initVarName = 97;
                }
                if (type.equals("String")) {
                    variable.append(type).append(" ").append((char) (initVarName)).append(num).append("=\"").append(paramList.get(i)).append("\";");
                } else {
                    variable.append(type).append(" ").append((char) (initVarName)).append(num).append("=").append(paramList.get(i)).append(";");
                }
                varList.add((char) (initVarName) + String.valueOf(num));
                initVarName++;
                num++;
            }
            //출력문 생성
            generatedOut(variable, varList, ansTypeStr);
        }

    }

    @Override
    public void generatedOut(StringBuilder out, List<String> varList, String ansType) {
        boolean isArray = ansType.contains("[]");

        if (isArray) {
            out.append("System.out.println(Arrays.toString(s.solution(");
            for (int i = 0; i < varList.size(); i++) {
                if (i == varList.size() - 1) {
                    out.append(varList.get(i));
                } else {
                    out.append(varList.get(i)).append(",");
                }
            }
            out.append("))+\"_\");");
        } else {
            out.append("System.out.println(s.solution(");
            for (int i = 0; i < varList.size(); i++) {
                if (i == varList.size() - 1) {
                    out.append(varList.get(i));
                } else {
                    out.append(varList.get(i)).append(",");
                }
            }
            out.append(")+\"_\");");
        }
    }
}
