package com.seventeam.algoritmgameproject.web.dto.game_dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class CreateRoomRequestDto {
    @Schema(type = "int", example = "0",description = "0:JAVA, 1:JS, 2:PYTHON3")
    int langIdx;
    @Schema(type = "int", example = "0",description = "0:EASY, 1:NORMAL, 2:HARD")
    int levelIdx;

    public CreateRoomRequestDto(int langIdx, int levelIdx) {
        this.langIdx = langIdx;
        this.levelIdx = levelIdx;
    }
}
