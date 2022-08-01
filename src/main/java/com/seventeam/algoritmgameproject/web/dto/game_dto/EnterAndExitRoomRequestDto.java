package com.seventeam.algoritmgameproject.web.dto.game_dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class EnterAndExitRoomRequestDto {
    @Schema(type = "String", example = "esg-sgdsg-egs",description = "방 생성시 발급한 UUID")
    private String roomId;
    @Schema(type = "String", example = "JAVAEASY",description = "방 생성시 발급한 서버이름")
    private String server;

    public EnterAndExitRoomRequestDto(String roomId, String server) {
        this.roomId = roomId;
        this.server = server;
    }
}
