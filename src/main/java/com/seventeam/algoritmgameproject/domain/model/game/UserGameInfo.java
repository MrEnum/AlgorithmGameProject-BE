package com.seventeam.algoritmgameproject.domain.model.game;

import lombok.*;

import java.io.Serializable;

@Getter@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserGameInfo implements Serializable {
    private static final long serialVersionUID = 6494678977089006628L;

    private final String type = "USERINFO";
    private String playerName;
    private String profileUrl;
    private int winCnt;
    private int loseCnt;


    @Builder
    private UserGameInfo(String playerName, String profileUrl, int winCnt, int loseCnt) {
        this.playerName = playerName;
        this.profileUrl = profileUrl;
        this.winCnt = winCnt;
        this.loseCnt = loseCnt;
    }

    public void setWinCnt(int winCnt) {
        this.winCnt = winCnt;
    }

    public void setLoseCnt(int loseCnt) {
        this.loseCnt = loseCnt;
    }
}
