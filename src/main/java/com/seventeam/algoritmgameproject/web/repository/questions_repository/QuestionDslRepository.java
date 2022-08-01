package com.seventeam.algoritmgameproject.web.repository.questions_repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seventeam.algoritmgameproject.domain.QuestionLevel;
import com.seventeam.algoritmgameproject.domain.model.questions.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.seventeam.algoritmgameproject.domain.model.questions.QQuestion.question1;


@Repository
@RequiredArgsConstructor

public class QuestionDslRepository {
    private final JPAQueryFactory query;

    @Transactional
    public List<Question> findAllQuestions() {
        List<Question> questions = query.selectFrom(question1)
                .leftJoin(question1.templates)
                .fetchJoin()
                .distinct().fetch();
        query.selectFrom(question1).leftJoin(question1.cases).fetchJoin().fetch();
        return questions;
    }

    @Transactional(readOnly = true)
    public List<Question> findAllByQuestionLevel(QuestionLevel level) {
        return query
                .selectFrom(question1)
                .where(question1.level.eq(level))
                .leftJoin(question1.templates)
                .fetchJoin()
                .leftJoin(question1.cases)
                .fetchJoin()
                .distinct()
                .fetch();
    }

    //추후 redis 저장 데이터로 셔플
    @Transactional(readOnly = true)
    public Question findOneQuestionByLevel(QuestionLevel level, Long id) {
        return query
                .selectFrom(question1)
                .where(question1.id.eq(id).and(question1.level.eq(level)))
                .leftJoin(question1.cases)
                .fetchJoin()
                .leftJoin(question1.templates)
                .fetchJoin()
                .fetchOne();
    }

    @Transactional(readOnly = true)
    public List<Long> findIdListByLevel(QuestionLevel level) {
        return query
                .select(question1.id).from(question1)
                .where(question1.level.eq(level))
                .orderBy(question1.id.asc())
                .fetch();
    }

}
