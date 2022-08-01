package com.seventeam.algoritmgameproject.domain.model.questions;


import lombok.*;

import javax.persistence.*;

@Entity
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TestCase {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QUESTION_ID")
    @ToString.Exclude
    private Question question;

    @Column(nullable = false, length = 5000)
    private String answer;

    @Column(nullable = false, length = 5000)
    private String params;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String ansType;

    @Column(nullable = false)
    private boolean isExample = false;
    @Builder
    private TestCase(Question question, String answer, String params, String type, String ansType, boolean isExample) {
        this.question = question;
        this.answer = answer;
        this.params = params;
        this.type = type;
        this.ansType = ansType;
        this.isExample = isExample;
    }

    public void setQuestion(Question question){
        this.question = question;
    }

    public void setExample(){
        this.isExample = true;
    }
}
