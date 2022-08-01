package com.seventeam.algoritmgameproject.web.service.login_service;

import com.seventeam.algoritmgameproject.domain.model.game.UserGameInfo;
import com.seventeam.algoritmgameproject.domain.model.login.GithubProfile;
import com.seventeam.algoritmgameproject.domain.model.login.OAuthToken;
import com.seventeam.algoritmgameproject.domain.model.login.User;
import com.seventeam.algoritmgameproject.security.JwtTokenProvider;
import com.seventeam.algoritmgameproject.web.dto.login_dto.UserResponseDto;
import com.seventeam.algoritmgameproject.web.repository.UserRedisRepository;
import com.seventeam.algoritmgameproject.web.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class GithubLoginService {
    private final static String TOKEN_REQUEST_URL = "https://github.com/login/oauth/access_token";
    private final static String PROFILE_REQUEST_URL = "https://api.github.com/user";
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final JwtTokenProvider provider;
    private final UserRedisRepository redisRepository;

    private final String client_id;

    private final String client_secret;

    public GithubLoginService(

            PasswordEncoder passwordEncoder,
            UserRepository repository,
            JwtTokenProvider provider,
            UserRedisRepository redisRepository, @Value("${client.id}") String client_id,
            @Value("${client.secret}") String client_secret) {

        this.passwordEncoder = passwordEncoder;
        this.repository = repository;
        this.provider = provider;
        this.redisRepository = redisRepository;
        this.client_id = client_id;
        this.client_secret = client_secret;
    }


    @Transactional
    public UserResponseDto getGithubProfile(OAuthToken oAuthToken) {

        RestTemplate profileRequestTemplate = new RestTemplate();
        ResponseEntity<GithubProfile> profileResponse = profileRequestTemplate.exchange(
                PROFILE_REQUEST_URL,
                HttpMethod.GET,
                getProfileRequestEntity(oAuthToken),
                GithubProfile.class
        );

        GithubProfile profile = profileResponse.getBody();

        assert profile != null;
        User user;
        boolean newUser = true;
        if (repository.existsByUserId(profile.getLogin())) {
            Optional<User> byUserId = repository.findByUserId(profile.getLogin());
            user = byUserId.orElseThrow(() -> new NullPointerException("없는 사용자 입니다."));
            newUser = false;
        } else {
            user = profileToUser(profile);
        }

        if (redisRepository.findUser(user.getUserId()) != null) {
            redisRepository.deleteUserGameInfoCache(user.getUserId());
            throw new RuntimeException("이미 로그인 중입니다.\n 로그아웃 됩니다.");
        }

        UserGameInfo userGameInfo = UserGameInfo.builder()
                .playerName(user.getUserId())
                .profileUrl(user.getAvatarUrl())
                .winCnt(user.getWinCnt())
                .loseCnt(user.getLoseCnt()).build();

        redisRepository.saveOrUpdateUserGameInfoCache(userGameInfo);

        return UserResponseDto.builder()
                .token(provider.createToken(user))
                .username(user.getUserId())
                .profile(user.getAvatarUrl())
                .winCnt(user.getWinCnt())
                .loseCnt(user.getLoseCnt())
                .newUser(newUser)
                .build();


    }

    //사용자 정보 얻는 부분
    private HttpEntity<MultiValueMap<String, String>> getProfileRequestEntity(OAuthToken oAuthToken) {
        HttpHeaders infoRequestHeaders = new HttpHeaders();
        infoRequestHeaders.add("Authorization", "token " + oAuthToken.getAccesToken());
        return new HttpEntity<>(infoRequestHeaders);
    }

    //토근 요청 부분
    public OAuthToken getOAuthToken(String code) {
        ResponseEntity<OAuthToken> response;
        try {

            RestTemplate tokenRequestTemplate = new RestTemplate();
            response = tokenRequestTemplate.exchange(TOKEN_REQUEST_URL,
                    HttpMethod.POST,
                    getCodeRequestHttpEntity(code),
                    OAuthToken.class);
        } catch (Exception e) {
            throw new RuntimeException("인증 코드가 올바르지 않습니다.");
        }
        return response.getBody();
    }

    //parameter 생성 부분
    private HttpEntity<MultiValueMap<String, String>> getCodeRequestHttpEntity(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", client_id);
        params.add("client_secret", client_secret);
        params.add("code", code);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        return new HttpEntity<>(params, headers);
    }

    User profileToUser(GithubProfile profile) {
        User user = User.builder()
                .userId(profile.getLogin())
                .avatarUrl(profile.getAvatarUrl())
                .email(profile.getEmail())
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .build();

        return repository.save(user);
    }
}
