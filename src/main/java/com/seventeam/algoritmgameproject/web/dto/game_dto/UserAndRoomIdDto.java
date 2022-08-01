package com.seventeam.algoritmgameproject.web.dto.game_dto;

import com.seventeam.algoritmgameproject.domain.model.game.UserGameInfo;
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
    private String to;

    public UserAndRoomIdDto(String roomId, UserGameInfo info, String to) {
        this.roomId = roomId;
        this.info = info;
        this.to = to;
    }
}
