package com.seventeam.algoritmgameproject.web.repository.game_repository;

import com.seventeam.algoritmgameproject.web.dto.questions_dto.QuestionRedis;
import org.springframework.data.repository.CrudRepository;

public interface QuestionsRedisRepository extends CrudRepository<QuestionRedis, Long> {
}
