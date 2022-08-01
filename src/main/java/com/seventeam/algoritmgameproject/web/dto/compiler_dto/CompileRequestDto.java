package com.seventeam.algoritmgameproject.web.dto.compiler_dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CompileRequestDto {

    @Schema(type = "String",description = "roomID")
    private String roomId;
    @Schema(type = "long",example = "1")
    private long questionId;
    @Schema(type = "int",example = "0",description = "0:JAVA, 1:JS, 2:PYTHON3")
    private int languageIdx;
    @Schema(type = "String",example = "def solution(){}")
    private String codeStr;

    public CompileRequestDto(String roomId, long questionId, int languageIdx, String codeStr) {
        this.roomId = roomId;
        this.questionId = questionId;
        this.languageIdx = languageIdx;
        this.codeStr = codeStr;
    }
}
