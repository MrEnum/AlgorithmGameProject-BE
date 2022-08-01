package com.seventeam.algoritmgameproject.web.repository.game_repository;


import com.seventeam.algoritmgameproject.domain.QuestionLevel;
import com.seventeam.algoritmgameproject.domain.model.game.UserGameInfo;
import com.seventeam.algoritmgameproject.web.service.compiler_service.Language;
import com.seventeam.algoritmgameproject.domain.model.game.GameRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Repository
@Slf4j
public class GameRoomRepository {


    private static final String FIND_SERVER="FIND_SERVER";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, GameRoom> hashOpsGameRoom;

    @PostConstruct
    void init() {
        hashOpsGameRoom = redisTemplate.opsForHash();
    }


    public List<GameRoom> findIsEnterAndSelectedLangAndLevelRooms(String server) {

        List<GameRoom> values = hashOpsGameRoom.values(server);
        List<GameRoom> isEnterRooms = new ArrayList<>();
        for (GameRoom value : values) {
            if (value.isEnter()) {

                isEnterRooms.add(value);
            }
        }
        return isEnterRooms;
    }

    public GameRoom findRoomById(String server, String roomId) {
        return hashOpsGameRoom.get(server, roomId);
    }

    public String findServer(String roomId) {
        log.info("방 이름으로 서버 찾는 중:{}", roomId);
        log.info("찾은 서버 이름:{}", Objects.requireNonNull(redisTemplate.opsForValue().get(FIND_SERVER + roomId)));
        return Objects.requireNonNull(redisTemplate.opsForValue().get(FIND_SERVER + roomId)).toString();
    }
    public void deleteServerRoomData(String roomId){
        redisTemplate.delete(FIND_SERVER + roomId);
    }

    //게임방 생성
    public GameRoom createGameRoom(Language language, QuestionLevel level, UserGameInfo creator) {

        GameRoom gameRoom = GameRoom.builder()
                .language(language.name())
                .questionLevel(level.name())
                .creatorGameInfo(creator)
                .build();

        log.info("서버에 방생성:{}",FIND_SERVER + gameRoom.getRoomId());
        redisTemplate.opsForValue().set(FIND_SERVER + gameRoom.getRoomId(), gameRoom.getServer());

        hashOpsGameRoom.put(
                language.name() + level.name(),
                gameRoom.getRoomId(),
                gameRoom
        );

        return gameRoom;
    }
    
    public void deleteRoom(String server, String roomId) {
        hashOpsGameRoom.delete(server, roomId);
    }

    public void changeCreator(GameRoom room, UserGameInfo userGameInfo) {
        room.changeCreator(userGameInfo);
        hashOpsGameRoom.put(room.getServer(), room.getRoomId(), room);
        log.info("방장 정보 갱신:{}",room.getCreatorGameInfo().getPlayerName());
    }

    public void enterAndExitGameRoom(GameRoom room, boolean enter) {
        if (enter) {
            room.setEnter();
            hashOpsGameRoom.putIfAbsent(
                    room.getServer(),
                    room.getRoomId(),
                    room
            );
        } else {
            room.setExit();
            hashOpsGameRoom.putIfAbsent(
                    room.getServer(),
                    room.getRoomId(),
                    room
            );
            log.info("방장 나가기 처리 완료");
        }
    }

}
