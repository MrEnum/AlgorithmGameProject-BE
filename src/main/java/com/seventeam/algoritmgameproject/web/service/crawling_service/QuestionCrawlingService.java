package com.seventeam.algoritmgameproject.web.service.crawling_service;

import com.seventeam.algoritmgameproject.domain.QuestionLevel;
import com.seventeam.algoritmgameproject.domain.model.questions.Question;

public interface QuestionCrawlingService {
    //기본 문제 저장
    void saveDefaultQuestions();

    void initQuestionIdByLevel();
    //문제 추가
    Question uploadQuestion(String questionUrl, QuestionLevel level);
}
