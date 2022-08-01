package com.seventeam.algoritmgameproject.web.service.compiler_service.generatedTemplate;

import com.seventeam.algoritmgameproject.web.dto.questions_dto.TestCaseRedis;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GeneratedJavascriptTemplateImp implements GeneratedTemplate {
    private final GeneratedTemplateUtil loop;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public String compileCode(String codeStr, List<TestCaseRedis> testCases, Long questionId) {

        if(codeStr.contains("console.log")){
            throw new IllegalArgumentException("출력문을 작성하지 마세요!");
        }

        if (!codeStr.contains("solution")) {
            int start;
            int end;
            if(codeStr.contains("function")) {
                start = codeStr.indexOf("n");
                end = codeStr.indexOf("(");

            }else{
                start = codeStr.indexOf("t");
                end = codeStr.indexOf("=");

            }
            String methodName = codeStr.substring(start + 1, end).trim();
            codeStr = codeStr.replace(methodName, "solution");
        }
        StringBuilder buffer;
        buffer = new StringBuilder(codeStr);

        buffer.append(";");

        if(Boolean.TRUE.equals(redisTemplate.hasKey("jsTemplate" + questionId))){
            buffer.append(redisTemplate.opsForValue().get("jsTemplate"+questionId));
        }else{
            redisTemplate.opsForValue().set("jsTemplate"+questionId, addTestCode(testCases));
            buffer.append(addTestCode(testCases));
        }
        return buffer.toString();
    }

    @Override
    public String addTestCode(List<TestCaseRedis> testCases) {
        StringBuilder buffer = new StringBuilder(50);
        generatedTemplate(buffer, testCases);
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

        for (TestCaseRedis testCase : testCases) {
            List<String> varList = new ArrayList<>();
            String params = testCase.getParams();
            String typeStr = testCase.getType();

            if (typeStr.equals("String")) {
                variable.append("let").append(" ").append((char) (initVarName)).append(num).append("=\"").append(params).append("\";");
            } else {
                variable.append("let").append(" ").append((char) (initVarName)).append(num).append("=").append(params).append(";");
            }
            if (initVarName == 122) {
                initVarName = 97;
            }
            //변수 생성 및 초기화
            varList.add((char) (initVarName) + String.valueOf(num));
            initVarName++;
            num++;
            //출력문 생성
            generatedOut(variable, varList, null);
        }
    }

    @Override
    public void generatedAboutSameMultiParam(StringBuilder variable, List<TestCaseRedis> testCases) {
        int initVarName = 97;
        int num = 0;

        for (TestCaseRedis testCase : testCases) {
            ArrayList<String> varList = new ArrayList<>();
            String params;
            params = testCase.getParams();
            String typeStr = testCase.getType();

            String[] tmp = typeStr.split("_");
            List<String> paramList = new ArrayList<>();
            String type = tmp[0];
            int cnt = Integer.parseInt(tmp[1]);

            //배열 확인
            if (type.contains("[]")) {
                String[] ans = params.trim().split("_");
                paramList.addAll(Arrays.asList(ans));
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
                    variable.append("let").append(" ").append((char) (initVarName)).append(num).append("=\"").append(paramList.get(i)).append("\";");
                } else {
                    variable.append("let").append(" ").append((char) (initVarName)).append(num).append("=").append(paramList.get(i)).append(";");
                }
                varList.add((char) (initVarName) + String.valueOf(num));
                initVarName++;
                num++;
            }
            //출력문 생성
            generatedOut(variable, varList,null);
        }
    }

    @Override
    public void generatedOut(StringBuilder out, List<String> variables, String ansType) {
        out.append("console.log(solution(");
        loop.variableLoop(out,variables);
        out.append(")+\"_\");\n");
    }
}
