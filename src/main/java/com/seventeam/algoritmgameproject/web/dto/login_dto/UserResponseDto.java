package com.seventeam.algoritmgameproject.web.dto.login_dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponseDto {
    private String token;
    private String username;
    private String profile;
    private int winCnt;
    private int loseCnt;
    private boolean newUser;

    @Builder
    private UserResponseDto(String token, String username, String profile, int winCnt, int loseCnt, boolean newUser) {
        this.token = token;
        this.username = username;
        this.profile = profile;
        this.winCnt = winCnt;
        this.loseCnt = loseCnt;
        this.newUser = newUser;
    }
}
