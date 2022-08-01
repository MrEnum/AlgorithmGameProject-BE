//package com.seventeam.algoritmgameproject.service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.seventeam.algoritmgameproject.domain.QuestionLevel;
//import com.seventeam.algoritmgameproject.domain.model.questions.Question;
//import com.seventeam.algoritmgameproject.domain.model.questions.TestCase;
//import com.seventeam.algoritmgameproject.web.dto.questions_dto.QuestionRedis;
//import com.seventeam.algoritmgameproject.web.dto.questions_dto.QuestionsByLevel;
//import com.seventeam.algoritmgameproject.web.dto.questions_dto.TestCaseRedis;
//import com.seventeam.algoritmgameproject.web.repository.game_repository.QuestionsIdListByLevelRepository;
//import com.seventeam.algoritmgameproject.web.repository.game_repository.QuestionsRedisRepository;
//import com.seventeam.algoritmgameproject.web.repository.game_repository.TestCaseRedisRepository;
//import com.seventeam.algoritmgameproject.web.repository.questions_repository.QuestionDslRepository;
//import com.seventeam.algoritmgameproject.web.repository.UserRepository;
//import com.seventeam.algoritmgameproject.web.service.game_service.GameService;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.*;
//
//@Slf4j
//@ActiveProfiles("testDB")
//@SpringBootTest
//@ExtendWith(SpringExtension.class)
//@Transactional
//@Rollback
//public class RepositoryTest {
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    QuestionDslRepository repository;
//
//    @Autowired
//    QuestionDslRepository questionDslRepository;
//
//    @Autowired
//    RedisTemplate<String, Object> redisTemplate;
//
//    @Autowired
//    QuestionsRedisRepository redisRepository;
//
//    @Autowired
//    TestCaseRedisRepository testCaseRedisRepository;
//    @Autowired
//    GameService gameService;
//
//    @Autowired
//    ObjectMapper mapper;
//
//    @Autowired
//    QuestionsIdListByLevelRepository questionsIdListByLevelRepository;
//
//    @Test
//    @DisplayName("승리 처리")
//    public void test0() {
//
////        User winUser = userRepository.findByUserId("biolkj28").orElseThrow(() -> new NullPointerException("없는 유저 입니다."));
////        log.info("사용자 정보: {}", winUser.toString());
////        User save = null;
////
////        winUser.win();
////        save = userRepository.save(winUser);
////
////        log.info("저장 승리 처리 사용자:{}", save.toString());
//    }
//
//    @Test
//    @DisplayName("패배 처리")
//    public void test1() {
//
////        User loseUser = userRepository.findByUserId("biolkj28").orElseThrow(() -> new NullPointerException("없는 유저 입니다."));
////        log.info("사용자 정보: {}", loseUser.toString());
////        User save = null;
////
////        loseUser.lose();
////        save = userRepository.save(loseUser);
////
////        log.info("저장 패배 처리 사용자:{}", save.toString());
//    }
//
//    @Test
//    @DisplayName("문제 데이터 캐시 처리")
//    public void test2() {
//
//        List<Question> allByQuestionLevel = repository.findAllQuestions();
//        List<Long> easy = new ArrayList<>();
//        List<Long> normal = new ArrayList<>();
//        List<Long> hard = new ArrayList<>();
//
//        for (Question question : allByQuestionLevel) {
//
//            if (redisRepository.existsById(question.getId())) continue;
//            // 난이도 별 문제 id 리스트 저장
//            switch (question.getLevel().getValue()) {
//                case 0: {
//                    easy.add(question.getId());
//                    break;
//                }
//                case 1: {
//                    normal.add(question.getId());
//                    break;
//                }
//                case 2: {
//                    hard.add(question.getId());
//                    break;
//                }
//
//            }
//            //문제 저장
//            QuestionRedis qs = new QuestionRedis();
//            BeanUtils.copyProperties(question, qs);
//            List<Long> casesIds = new ArrayList<>();
//            int cnt = 0;
//
//            // 테스트 케이스 저장
//            for (TestCase aCase : question.getCases()) {
//                if (cnt == 10) break;
//                casesIds.add(aCase.getId());
//
//                TestCaseRedis tc = new TestCaseRedis();
//                tc.setQuestionId(aCase.getId());
//                BeanUtils.copyProperties(aCase, tc);
//                TestCaseRedis save = testCaseRedisRepository.save(tc);
//
//                log.info("testCase:{}", save);
//                cnt++;
//            }
//            qs.setCasesIds(casesIds);
//            redisRepository.save(qs);
//
//        }
//
//        //난이도 별 문제 id 저장
//        String[] genre = new String[]{"EASY", "NORMAL", "HARD"};
//        List<QuestionsByLevel> questionsIdsList = new ArrayList<>();
//        for (int i = 0; i < genre.length; i++) {
//            QuestionsByLevel level = new QuestionsByLevel();
//            level.setId(genre[i]);
//            if (i == 0) {
//                level.setQuestions(easy);
//            } else if (i == 1) {
//                level.setQuestions(normal);
//            } else {
//                level.setQuestions(hard);
//            }
//            questionsIdsList.add(level);
//
//        }
//        questionsIdListByLevelRepository.saveAll(questionsIdsList);
//    }
//
//    @Test
//    @DisplayName("랜덤 문제 뽑기")
//    public void test3() {
////        QuestionsByLevel easy1 = questionsIdListByLevelRepository.findById("EASY")
////                .orElseThrow(() -> new NullPointerException("데이터 없음"));
////        Collections.shuffle(easy1.getQuestions());
////        log.info("EASY IDS:{}", easy1.getQuestions().toString());
////        log.info("random questions:{}", easy1.getQuestions().get(0));
//
//        List<Long> idListByLevel = questionDslRepository.findIdListByLevel(QuestionLevel.EASY);
//        List<Long> casesIds = new ArrayList<>();
////        if (level.equals(QuestionLevel.HARD)) {
////            idListByLevel.remove(idListByLevel.size() - 1);
////        }
//        Collections.shuffle(idListByLevel);
//        Question oneQuestionByLevel = questionDslRepository.findOneQuestionByLevel(QuestionLevel.EASY, idListByLevel.get(0));
//        QuestionRedis redis = new QuestionRedis();
//        BeanUtils.copyProperties(oneQuestionByLevel,redis);
//
//        log.info("출력:{}",redis.toString());
//    }
//
//    @Test
//    @DisplayName("테스트 케이스 목록 들고오기")
//    public void test4()  {
////        QuestionRedis byId = redisRepository.findById(1L).orElseThrow(() -> new NullPointerException("데이터 없음"));
////        Iterable<TestCaseRedis> allById = testCaseRedisRepository.findAllById(byId.getCasesIds());
////        List<TestCaseRedis> testcases = new ArrayList<>();
////        allById.forEach(testcases::add);
////        log.info(testcases.toString());
//        QuestionRedis redis = gameService.randomQuestions(QuestionLevel.EASY);
//        log.info("랜덤 질문 체크:{}",redis.toString());
//
//    }
//}
