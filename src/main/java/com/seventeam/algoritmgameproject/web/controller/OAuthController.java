package com.seventeam.algoritmgameproject.web.controller;


import com.seventeam.algoritmgameproject.domain.model.login.OAuthToken;
import com.seventeam.algoritmgameproject.web.dto.login_dto.UserResponseDto;
import com.seventeam.algoritmgameproject.web.repository.UserRedisRepository;
import com.seventeam.algoritmgameproject.web.service.login_service.GithubLoginService;
import com.seventeam.algoritmgameproject.web.service.login_service.UserDetailImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OAuthController {
    private final GithubLoginService service;
    private final Environment env;
    private final UserRedisRepository userRedisRepository;

    @GetMapping("/login/oauth2/code/github")
    public UserResponseDto githubLogin(String code) {
        OAuthToken oAuthToken = service.getOAuthToken(code);
        return service.getGithubProfile(oAuthToken);
    }
    @GetMapping("/chinda/logout")
    public void logout(@AuthenticationPrincipal UserDetailImpl userDetail){
        userRedisRepository.deleteUserGameInfoCache(userDetail.getUsername());
    }

    @GetMapping("/profile")
    @Operation(summary = "무중단 배포 확인용(무시해도됨)")
    public String getProfile() {
        return Arrays.stream(env.getActiveProfiles())
                .findFirst()
                .orElse("");
    }

}