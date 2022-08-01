package com.seventeam.algoritmgameproject.web.repository.questions_repository;

import com.seventeam.algoritmgameproject.domain.model.questions.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
}
