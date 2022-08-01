package com.seventeam.algoritmgameproject.web.dto.game_dto;

import com.seventeam.algoritmgameproject.domain.model.game.GameRoom;
import com.seventeam.algoritmgameproject.domain.model.game.UserGameInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter@ToString
@NoArgsConstructor
public class FindRoomsResult {
    private UserGameInfo userGameInfo;
    private List<GameRoom> gameRooms;

    public FindRoomsResult(UserGameInfo userGameInfo, List<GameRoom> gameRooms) {
        this.userGameInfo = userGameInfo;
        this.gameRooms = gameRooms;
    }
}
