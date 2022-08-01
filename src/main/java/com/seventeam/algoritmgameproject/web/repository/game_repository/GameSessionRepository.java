package com.seventeam.algoritmgameproject.web.repository.game_repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
@Repository
@Slf4j
public class GameSessionRepository {
    public static final String PARTICIPANT = "PARTICIPANT";
    public static final String CREATOR = "CREATOR";
    public static final String FINDER = "FINDER";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, String> hashOpsEnterInfo;

    @PostConstruct
    void init() {
        hashOpsEnterInfo = redisTemplate.opsForHash();
    }

    public void saveEnterSession(String roomId, String username, String role) {
        hashOpsEnterInfo.putIfAbsent(roomId, username, role);
    }
    public void deleteSession(String roomId, String username){
        hashOpsEnterInfo.delete(roomId, username);
    }
    public String findOthersSession(String roomId, String mine) {
        Set<String> keys = hashOpsEnterInfo.keys(roomId);
        String to = null;
        for (String value : keys) {
            if (!value.equals(mine)) {
                to = value;
                break;
            }
        }
        return to;
    }
    public String findRoomIdBySession(String username){
        return Objects.requireNonNull(redisTemplate.opsForValue().get(FINDER + username)).toString();
    }
    public boolean notYetExit(String username){
        return Boolean.TRUE.equals(redisTemplate.hasKey(FINDER + username));
    }
    public void saveRoomIdBySession(String username, String roomId){
        redisTemplate.opsForValue().set(FINDER + username, roomId);
    }
    public void deleteRoomIdBySession(String username){
        redisTemplate.delete(FINDER + username);
    }
    public Long roomEnterCnt(String roomId){
        return hashOpsEnterInfo.size(roomId);
    }
    public String getRole(String roomId, String username){
        return hashOpsEnterInfo.get(roomId, username);
    }
    public void upgradeRole(String roomId,String username){
        hashOpsEnterInfo.put(roomId, username, CREATOR);
    }
    public List<String> findSessions(String roomId) {
        return hashOpsEnterInfo.values(roomId);
    }
}
