package com.seventeam.algoritmgameproject.web.service.compilerService.generatedTemplate;

import com.seventeam.algoritmgameproject.domain.model.TestCase;

import java.util.List;

public interface GeneratedTemplate {
    String compileCode(String codeStr, List<TestCase> testCases,Long questionId);

    // 메인 메소드 추가, 변수 추가, 출력문 추가
    String addTestCode(List<TestCase> testCases);

    //인자 개수에 따른 메서드 선택
    void generatedTemplate(StringBuilder variable, List<TestCase> testCases);

    // 같은 타입 문자열 복수개 일 때 변수 및 초기화 문자열 생성
    void generatedAboutSingleParam(StringBuilder variable, List<TestCase> testCases);

    // 같은 타입 문자열 복수개 일 때 변수 및 초기화 문자열 생성
    void generatedAboutSameMultiParam(StringBuilder variable, List<TestCase> testCases);

    // ex) System.out.println(Arrays.toString(s.solution(a,b)) 문자열 생성
    void generatedOut(StringBuilder out, List<String> varList, String ansType);
}
