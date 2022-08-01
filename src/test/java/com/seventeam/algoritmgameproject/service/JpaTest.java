//package com.seventeam.algoritmgameproject.service;
//
//import com.seventeam.algoritmgameproject.domain.QuestionLevel;
//import com.seventeam.algoritmgameproject.domain.model.questions.Question;
//import com.seventeam.algoritmgameproject.domain.model.questions.TestCase;
//import com.seventeam.algoritmgameproject.domain.repository.QuestionDslRepository;
//import com.seventeam.algoritmgameproject.domain.model.game.GameRoom;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//
//import java.util.List;
//
//@SpringBootTest
//public class JpaTest {
//
//    @Autowired
//    QuestionDslRepository repoRepository;
//
//    @Autowired
//    RedisTemplate<String, Object> redisTemplate;
//    private HashOperations<String, String, Question> gameRoomHashOperations;
//
//    @BeforeEach
//    void init(){
//        gameRoomHashOperations = redisTemplate.opsForHash();
//    }
//    @Test
//    @DisplayName("문제 조회 테스트")
//    void findAll(){
//        List<Question> allQuestions = repoRepository.findAllQuestions();
//    }
//
//    @Test
//    @DisplayName("난이도로 문제 랜덤 조회 테스트")
//    void findLevel(){
//        Question randomQuestionLevel = repoRepository.findRandomQuestionLevel(QuestionLevel.EASY);
//        System.out.println(randomQuestionLevel.getQuestion());
//        for (TestCase aCase : randomQuestionLevel.getCases()) {
//            System.out.println(aCase.toString());
//        }
//        randomQuestionLevel.getTemplates().values().forEach(System.out::println);
//    }
//
//}
