package com.seventeam.algoritmgameproject;


import com.seventeam.algoritmgameproject.web.service.compiler_service.generatedTemplate.InitQuestionsCache;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BaseQuestionsInit implements ApplicationRunner {
    //private final QuestionCrawlingService service;
    private final InitQuestionsCache initQuestionsCache;
    @Override
    public void run(ApplicationArguments args) {
        //service.saveDefaultQuestions();
        initQuestionsCache.loadingData();
    }
}
