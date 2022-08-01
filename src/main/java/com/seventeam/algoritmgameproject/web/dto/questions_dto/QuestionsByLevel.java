package com.seventeam.algoritmgameproject.web.dto.questions_dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Getter@Setter@ToString
@RedisHash("QUESTIONS_ID_BY_LEVEL")
public class QuestionsByLevel {

    @Id
    private String id;
    private List<Long> questions = new ArrayList<>();

    public void add(Long id){
        this.questions.add(id);
    }
}
