package com.seventeam.algoritmgameproject.web.service.crawling_service;

import com.seventeam.algoritmgameproject.domain.model.questions.TestCase;

import java.util.*;

/*
 * 문제별 테스트케이스 자동생성 로직
 * */
public interface SolutionService {

    // 1번
    Set<TestCase> solution1ByEx();
    Set<TestCase> solution1TestCases();

    //2번
    Set<TestCase> solution2ByEx();
    Set<TestCase> solution2TestCases();

    //3번 핸드폰 번호 가리기
    Set<TestCase> solution3ByEx();
    Set<TestCase> solution3TestCases();

    //4번 x만큼 간격이 있는 n개의 숫자
    Set<TestCase> solution4ByEx();
    Set<TestCase> solution4TestCases();

    /*
     * 중 문제
     */
    //5. 부족한 금액 계산하기
    Set<TestCase> solution5ByEx();
    Set<TestCase> solution5TestCases();

    // 문제 6번 2016년
    Set<TestCase> solution6ByEx();
    Set<TestCase> solution6TestCases();

    // 문제 7번 문자열 내 p와 y의 개수
    Set<TestCase> solution7ByEx();
    Set<TestCase> solution7TestCases();


    /*
     * 상 문제
     */
    //문제 8번
    Set<TestCase> solution8ByEx();
    Set<TestCase> solution8TestCases();

    //문제 9 같은 숫자는 싫어
    Set<TestCase> solution9ByEx();
    Set<TestCase> solution9TestCases();

    //10번 로또의 최고 순위와 최저 순위
    Set<TestCase> solution10ByEx();
    Set<TestCase> solution10TestCases();

    Set<TestCase> generatedTestCases(int i);
}
