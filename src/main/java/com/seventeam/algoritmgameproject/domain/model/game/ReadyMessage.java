package com.seventeam.algoritmgameproject.domain.model.game;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReadyMessage {
    private final String type = "READY";
    private String server;
    private String roomId;
    private String sender;

    private Long questionId;
    private String title;
    private String question;
    private String template;

}
