package com.seventeam.algoritmgameproject.domain.model.game;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class GameMessage {
    private final String type = "GAME";
    private String roomId; // 방번호
    private String sender; // 메시지 보낸사람
    private String to;
    private String message; // 메시지

    public GameMessage(String roomId, String sender, String to, String message) {
        this.roomId = roomId;
        this.sender = sender;
        this.to = to;
        this.message = message;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
