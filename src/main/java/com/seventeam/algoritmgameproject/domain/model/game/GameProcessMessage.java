package com.seventeam.algoritmgameproject.domain.model.game;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GameProcessMessage implements Serializable {
    private static final long serialVersionUID = 6283278977089006639L;
    @Getter
    @NoArgsConstructor
    public static class Request{
        private MessageType type;
        private String username;
        private String roomId;
        private String to;

        public Request(MessageType type, String username, String roomId) {
            this.type = type;
            this.username = username;
            this.roomId = roomId;
        }

        public void setTo(String to) {
            this.to = to;
        }
    }
    @Getter
    @NoArgsConstructor
    public static class Response{
        private MessageType type;
        private String msg;

        public Response(MessageType type) {
            this.type = type;
            this.msg = type.getState();
        }
    }

}
