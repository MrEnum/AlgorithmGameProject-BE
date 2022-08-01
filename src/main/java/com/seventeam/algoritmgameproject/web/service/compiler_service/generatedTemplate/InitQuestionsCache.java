package com.seventeam.algoritmgameproject.web.service.compiler_service.generatedTemplate;

import com.seventeam.algoritmgameproject.domain.model.questions.Question;
import com.seventeam.algoritmgameproject.domain.model.questions.TestCase;
import com.seventeam.algoritmgameproject.web.dto.questions_dto.QuestionRedis;
import com.seventeam.algoritmgameproject.web.dto.questions_dto.QuestionsByLevel;
import com.seventeam.algoritmgameproject.web.dto.questions_dto.TestCaseRedis;
import com.seventeam.algoritmgameproject.web.repository.game_repository.QuestionsIdListByLevelRepository;
import com.seventeam.algoritmgameproject.web.repository.game_repository.QuestionsRedisRepository;
import com.seventeam.algoritmgameproject.web.repository.game_repository.TestCaseRedisRepository;
import com.seventeam.algoritmgameproject.web.repository.questions_repository.QuestionDslRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitQuestionsCache {
    private final QuestionDslRepository repository;
    private final QuestionsRedisRepository redisRepository;
    private final QuestionsIdListByLevelRepository questionsIdListByLevelRepository;
    private final TestCaseRedisRepository testCaseRedisRepository;

    public void loadingData() {

        List<Question> allByQuestionLevel = repository.findAllQuestions();
        List<Long> easy = new ArrayList<>();
        List<Long> normal = new ArrayList<>();
        List<Long> hard = new ArrayList<>();

        for (Question question : allByQuestionLevel) {
            if (redisRepository.existsById(question.getId())) continue;
            // 난이도 별 문제 id 리스트 저장
            switch (question.getLevel().getValue()) {
                case 0: {
                    easy.add(question.getId());
                    break;
                }
                case 1: {
                    normal.add(question.getId());
                    break;
                }
                case 2: {
                    hard.add(question.getId());
                    break;
                }

            }
            //문제 저장
            QuestionRedis qs = new QuestionRedis();
            BeanUtils.copyProperties(question, qs);
            List<Long> casesIds = new ArrayList<>();

            // 테스트 케이스 저장
            for (TestCase aCase : question.getCases()) {

                casesIds.add(aCase.getId());

                TestCaseRedis tc = new TestCaseRedis();
                tc.setQuestionId(aCase.getId());
                BeanUtils.copyProperties(aCase, tc);
                TestCaseRedis save = testCaseRedisRepository.save(tc);
                log.info("testCase:{}", save);

            }
            qs.setCasesIds(casesIds);
            redisRepository.save(qs);

        }

        //난이도 별 문제 id 저장
        String[] genre = new String[]{"EASY", "NORMAL", "HARD"};
        List<QuestionsByLevel> questionsIdsList = new ArrayList<>();
        for (int i = 0; i < genre.length; i++) {
            QuestionsByLevel level = new QuestionsByLevel();
            level.setId(genre[i]);
            if (i == 0) {
                level.setQuestions(easy);
            } else if (i == 1) {
                level.setQuestions(normal);
            } else {
                level.setQuestions(hard);
            }
            questionsIdsList.add(level);

        }
        questionsIdListByLevelRepository.saveAll(questionsIdsList);
    }
}
