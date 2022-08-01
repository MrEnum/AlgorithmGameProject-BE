package com.seventeam.algoritmgameproject.web.socketServer.model.Dto;

import com.seventeam.algoritmgameproject.web.socketServer.model.UserGameInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class UserAndRoomIdDto implements Serializable {

    private final String type = "USERINFO";
    private static final long serialVersionUID = 6494678978059006628L;
    private String roomId;
    private UserGameInfo info;

    public UserAndRoomIdDto(String roomId, UserGameInfo info) {
        this.roomId = roomId;
        this.info = info;
    }
}
