package com.seventeam.algoritmgameproject.web.dto.questions_dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@Getter@Setter
@ToString
@RedisHash("TESTCASE")
public class TestCaseRedis {

    @Id
    private Long id;
    private Long questionId;
    private String answer;
    private String params;
    private String type;
    private String ansType;

}
