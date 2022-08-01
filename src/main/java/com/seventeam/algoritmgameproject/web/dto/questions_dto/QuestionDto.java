package com.seventeam.algoritmgameproject.web.dto.questions_dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.seventeam.algoritmgameproject.domain.QuestionLevel;
import com.seventeam.algoritmgameproject.domain.model.questions.TestCase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class QuestionDto {

    private Long id;
    private String title;
    private String question;
    private String limitation;
    private String inOutExHead;
    private String inOutEx;
    private String inOutExDescription;
    private String reference;
    private QuestionLevel level;
    @JsonManagedReference
    private Map<String, String> templates = new HashMap<>();
    @JsonManagedReference
    private List<TestCase> cases = new ArrayList<>();

    public QuestionDto(Long id, String title, String question, String limitation, String inOutExHead, String inOutEx, String inOutExDescription, String reference, QuestionLevel level, Map<String, String> templates, List<TestCase> cases) {
        this.id = id;
        this.title = title;
        this.question = question;
        this.limitation = limitation;
        this.inOutExHead = inOutExHead;
        this.inOutEx = inOutEx;
        this.inOutExDescription = inOutExDescription;
        this.reference = reference;
        this.level = level;
        this.templates = templates;
        this.cases = cases;
    }
}
