package com.seventeam.algoritmgameproject.web.repository.game_repository;

import com.seventeam.algoritmgameproject.web.dto.questions_dto.QuestionsByLevel;
import org.springframework.data.repository.CrudRepository;

public interface QuestionsIdListByLevelRepository extends CrudRepository<QuestionsByLevel,String> {
}
