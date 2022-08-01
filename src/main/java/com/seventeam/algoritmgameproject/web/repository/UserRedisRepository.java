package com.seventeam.algoritmgameproject.web.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seventeam.algoritmgameproject.domain.model.game.UserGameInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRedisRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private static final String USER = "USER-";

    public UserGameInfo findUser(String username) {
        try {
            return objectMapper.readValue(redisTemplate.opsForValue().get(USER + username), UserGameInfo.class);
        } catch (Exception e) {
            return null;
        }
    }

    public void saveOrUpdateUserGameInfoCache(UserGameInfo userGameInfo){
        try {
            String object = objectMapper.writeValueAsString(userGameInfo);
            redisTemplate.opsForValue().set(USER + userGameInfo.getPlayerName(), object, 1000L * 60 * 30 * 2 * 5, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.info("유저 정보 저장 오류:{}",e.getMessage());
        }
    }

    public void deleteUserGameInfoCache(String username) {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(USER + username))) {
            redisTemplate.delete(USER + username);
        }
    }

}
