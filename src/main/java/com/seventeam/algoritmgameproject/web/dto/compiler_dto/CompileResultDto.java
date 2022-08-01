package com.seventeam.algoritmgameproject.web.dto.compiler_dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class CompileResultDto {

    @Schema(type = "String",description = "컴파일 정확도 또는 오류 메시지")
    private String msg;
    @Schema(type = "boolean",description = "true: 성공, false: 실패")
    private boolean result;

    @Builder
    public CompileResultDto(String msg, boolean result) {
        this.msg = msg;
        this.result = result;
    }

}
