package com.seventeam.algoritmgameproject.service.compilerService.generatedTemplate;

import com.seventeam.algoritmgameproject.domain.model.questions.TestCase;
import com.seventeam.algoritmgameproject.web.repository.questions_repository.TestCaseDslRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/*자바 문법에 맞는 출력문 문자열 추가 클래스*/
@Slf4j
public class GeneratedJavaTemplateImpTest {
    @Autowired
    private final TestCaseDslRepository repository;

    public GeneratedJavaTemplateImpTest(TestCaseDslRepository repository) {
        this.repository = repository;
    }

    public String compileCode(String codeStr, Long id) {

        //클래스명 변경
        if (!codeStr.contains("Solution")) {
            int start = codeStr.indexOf("s");
            int end = codeStr.indexOf("{");
            String methodName = codeStr.substring(start + 2, end).trim();
            codeStr = codeStr.replace(methodName, "Solution");
        }

        int i = codeStr.lastIndexOf('}');
        StringBuilder buffer = new StringBuilder(codeStr).deleteCharAt(i);
        List<TestCase> testCases = repository.getTestCases(id);
        String ansType = testCases.get(0).getAnsType();

        if (ansType.contains("[]") && !codeStr.contains("import java.util.Arrays;")) {
            buffer.insert(0, "import java.util.Arrays;");
        }

        buffer.append(addTestCode(testCases));
        return buffer.toString();
    }

    // 메인 메소드 추가, 변수 추가, 출력문 추가
    private String addTestCode(List<TestCase> testCases) {
        StringBuilder buffer = new StringBuilder(50);

        buffer.append("public static void main(String[] args) {");
        buffer.append("Solution s = new Solution();");

        generatedJavaTemplate(buffer, testCases);

        buffer.append("}}");
        return buffer.toString();
    }

    //인자 개수에 따른 메서드 선택 메서드
    private void generatedJavaTemplate(StringBuilder variable, List<TestCase> testCases) {
        String typeStr = testCases.get(0).getType();
        //같은 인자가 복수 개일 때
        if (typeStr.contains("_")) {
            generatedAboutSameMultiParamJava(variable, testCases);
        } else {
            //인자가 하나일 때, 서로 다른 타입은 추후 다른 문제 추가 시, 업데이트
            if (!typeStr.contains(",")) {
                generatedAboutSingleParamJava(variable, testCases);
            }
        }
    }

    // 같은 타입 문자열 복수개 일 때 변수 및 초기화 문자열 생성
    private void generatedAboutSingleParamJava(StringBuilder variable, List<TestCase> testCases) {

        int initVarName = 97;
        int num = 0;
        String ansTypeStr = testCases.get(0).getAnsType();

        for (TestCase testCase : testCases) {
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
            generatedOutJava(variable, varList, ansTypeStr);
        }

    }

    // 같은 타입 문자열 복수개 일 때 변수 및 초기화 문자열 생성
    private void generatedAboutSameMultiParamJava(StringBuilder variable, List<TestCase> testCases) {
        int initVarName = 97;
        int num = 0;
        String ansTypeStr = testCases.get(0).getAnsType();

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
            generatedOutJava(variable, varList, ansTypeStr);
        }

    }

    // ex) System.out.println(Arrays.toString(s.solution(a,b)) 문자열 생성
    private void generatedOutJava(StringBuilder out, List<String> varList, String ansType) {
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
            out.append(")));\n");
        } else {
            out.append("System.out.println(s.solution(");
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
}

