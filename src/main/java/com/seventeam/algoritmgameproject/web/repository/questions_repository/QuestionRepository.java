package com.seventeam.algoritmgameproject.web.repository.questions_repository;

import com.seventeam.algoritmgameproject.domain.model.questions.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question,Long>{
}
