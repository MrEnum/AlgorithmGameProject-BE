package com.seventeam.algoritmgameproject.web.dto.questions_dto;


import com.seventeam.algoritmgameproject.domain.QuestionLevel;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.util.*;

@Getter@Setter@ToString
@RedisHash("QUESTION")
public class QuestionRedis {
    @Id
    private Long id;
    private String title;
    private String question;
    private String limitation;
    private String inOutExHead;
    private String inOutEx;
    private String inOutExDescription;
    private String reference;
    private QuestionLevel level;
    private Map<String, String> templates = new HashMap<>();
    private List<Long> casesIds = new ArrayList<>();


}
