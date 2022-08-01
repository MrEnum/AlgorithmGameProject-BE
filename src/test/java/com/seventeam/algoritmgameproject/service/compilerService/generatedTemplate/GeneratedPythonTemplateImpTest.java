package com.seventeam.algoritmgameproject.service.compilerService.generatedTemplate;

import com.seventeam.algoritmgameproject.domain.model.questions.TestCase;
import com.seventeam.algoritmgameproject.web.repository.questions_repository.TestCaseDslRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GeneratedPythonTemplateImpTest {
    @Autowired
    private final TestCaseDslRepository repository;

    public GeneratedPythonTemplateImpTest(TestCaseDslRepository repository) {
        this.repository = repository;
    }

    public String compileCode(String codeStr, Long id) {

        //메소드명 표준으로 변경
        if (!codeStr.contains("solution")) {
            int start = codeStr.indexOf("f");
            int end = codeStr.indexOf("(");
            String methodName = codeStr.substring(start + 1, end).trim();
            codeStr = codeStr.replace(methodName, "solution");
        }
        StringBuilder buffer = new StringBuilder(codeStr);
        List<TestCase> testCases = repository.getTestCases(id);
        buffer.append("\n");
        buffer.append(addTestCode(testCases));
        return buffer.toString();
    }

    // 메인 메소드 추가, 변수 추가, 출력문 추가
    private String addTestCode(List<TestCase> testCases) {
        StringBuilder buffer = new StringBuilder(50);
        generatedPy3Template(buffer, testCases);
        return buffer.toString();
    }

    //인자 개수에 따른 메서드 선택 메서드
    private void generatedPy3Template(StringBuilder variable, List<TestCase> testCases) {
        String typeStr = testCases.get(0).getType();
        //같은 인자가 복수 개일 때
        if (typeStr.contains("_")) {
            generatedAboutSameMultiParamPy3(variable, testCases);
        } else {
            //인자가 하나일 때, 서로 다른 타입은 추후 다른 문제 추가 시, 업데이트
            if (!typeStr.contains(",")) {
                generatedAboutSingleParamPy3(variable, testCases);
            }
        }
    }

    // 같은 타입 문자열 복수개 일 때 변수 및 초기화 문자열 생성
    private void generatedAboutSingleParamPy3(StringBuilder variable, List<TestCase> testCases) {

        int initVarName = 97;
        int num = 0;

        for (TestCase testCase : testCases) {
            List<String> varList = new ArrayList<>();
            String params = testCase.getParams();
            String typeStr = testCase.getType();

            if (typeStr.equals("String")) {
                variable.append((char) (initVarName)).append(num).append("=\"").append(params).append("\";");
            } else {
                variable.append((char) (initVarName)).append(num).append("=").append(params).append(";");
            }
            if (initVarName == 122) {
                initVarName = 97;
            }
            //변수 생성 및 초기화
            varList.add((char) (initVarName) + String.valueOf(num));
            initVarName++;
            num++;
            //출력문 생성
            generatedOutPy3(variable, varList);
        }

    }

    // 같은 타입 문자열 복수개 일 때 변수 및 초기화 문자열 생성
    private void generatedAboutSameMultiParamPy3(StringBuilder variable, List<TestCase> testCases) {
        int initVarName = 97;
        int num = 0;

        for (TestCase testCase : testCases) {
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
                    variable.append((char) (initVarName)).append(num).append("=\"").append(paramList.get(i)).append("\"\n");
                } else {
                    variable.append((char) (initVarName)).append(num).append("=").append(paramList.get(i)).append("\n");
                }
                varList.add((char) (initVarName) + String.valueOf(num));
                initVarName++;
                num++;
            }
            //출력문 생성
            generatedOutPy3(variable, varList);
        }

    }

    // ex) System.out.println(Arrays.toString(s.solution(a,b)) 문자열 생성
    private void generatedOutPy3(StringBuilder out, List<String> varList) {

        out.append("print(solution(");
        for (int i = 0; i < varList.size(); i++) {
            if (i == varList.size() - 1) {
                out.append(varList.get(i));
            } else {
                out.append(varList.get(i)).append(",");
            }
        }
        out.append("));\n");

    }
}
