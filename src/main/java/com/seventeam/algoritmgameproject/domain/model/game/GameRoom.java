package com.seventeam.algoritmgameproject.domain.model.game;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameRoom implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;
    @Schema(type = "String", example = "esg-sgdsg-egs",description = "방 생성시 발급한 UUID")
    private String roomId;
    @Schema(type = "String", example = "JAVA",description = "선택한 언어")
    private String language;
    @Schema(type = "String", example = "EASY",description = "선택한 난이도")
    private String questionLevel;
    @Schema(type = "String", example = "JAVAEASY",description = "서버 이름")
    private String server;

    @Schema(type = "UserGameInfo",description = "방 생성 유저 정보")
    private UserGameInfo creatorGameInfo;
    @Schema(type = "boolean",description = "입장 가능 여부")
    private boolean isEnter = true;

    @Builder
    public GameRoom(String language, String questionLevel, UserGameInfo creatorGameInfo) {

        this.roomId = UUID.randomUUID().toString();
        this.language = language;
        this.questionLevel = questionLevel;
        this.creatorGameInfo = creatorGameInfo;
        this.server = language + questionLevel;
    }

    public void setEnter() {
        this.isEnter = false;
    }

    public void setExit() {
        this.isEnter = true;
    }
//    public void questionBlock(){
//        this.questionTitle = "";
//        this.question = "";
//    }

    public void changeCreator(UserGameInfo userGameInfo){
        this.creatorGameInfo = userGameInfo;
    }
}
