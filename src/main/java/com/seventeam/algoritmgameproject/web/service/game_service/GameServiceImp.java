package com.seventeam.algoritmgameproject.web.service.game_service;

import com.seventeam.algoritmgameproject.domain.QuestionLevel;
import com.seventeam.algoritmgameproject.domain.model.game.GameMessage;
import com.seventeam.algoritmgameproject.domain.model.game.GameRoom;
import com.seventeam.algoritmgameproject.domain.model.game.ReadyMessage;
import com.seventeam.algoritmgameproject.domain.model.game.UserGameInfo;
import com.seventeam.algoritmgameproject.domain.model.questions.Question;
import com.seventeam.algoritmgameproject.domain.model.login.User;
import com.seventeam.algoritmgameproject.domain.model.questions.TestCase;
import com.seventeam.algoritmgameproject.web.dto.game_dto.FindRoomsResult;
import com.seventeam.algoritmgameproject.web.dto.questions_dto.QuestionRedis;
import com.seventeam.algoritmgameproject.web.dto.questions_dto.QuestionsByLevel;
import com.seventeam.algoritmgameproject.web.repository.UserRedisRepository;
import com.seventeam.algoritmgameproject.web.repository.game_repository.QuestionsIdListByLevelRepository;
import com.seventeam.algoritmgameproject.web.repository.game_repository.QuestionsRedisRepository;
import com.seventeam.algoritmgameproject.web.repository.questions_repository.QuestionDslRepository;
import com.seventeam.algoritmgameproject.web.repository.UserRepository;
import com.seventeam.algoritmgameproject.web.dto.game_dto.UserAndRoomIdDto;
import com.seventeam.algoritmgameproject.web.service.compiler_service.Language;
import com.seventeam.algoritmgameproject.web.dto.game_dto.EnterAndExitRoomRequestDto;
import com.seventeam.algoritmgameproject.web.repository.game_repository.GameRoomRepository;
import com.seventeam.algoritmgameproject.web.repository.game_repository.GameSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameServiceImp implements GameService {
    private final GameRoomRepository gameRoomRepository;
    private final GameSessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final UserRedisRepository userRedisRepository;
    private final QuestionDslRepository questionDslRepository;
    private final QuestionsIdListByLevelRepository questionsIdListByLevelRepository;
    private final QuestionsRedisRepository questionsRedisRepository;
    private final ChannelTopic channelTopic;
    private final RedisTemplate<String, Object> redisTemplate;
    private final GameServiceUtil util;


    public static final String ROOM_READY = "ROOM_READY";
    private static final ConcurrentMap<String, Integer> readyMap = new ConcurrentHashMap<>();

    @Override
    public FindRoomsResult findRooms(int langIdx, int levelIdx, String username) {
        UserGameInfo userGameInfo = userRedisRepository.findUser(username);
        if (userGameInfo == null) {
            User dbUser = userRepository.findByUserId(username).orElseThrow(() -> new NullPointerException("사용자가 없습니다."));
            userGameInfo = userToUserInfo(dbUser);
            userRedisRepository.saveOrUpdateUserGameInfoCache(userGameInfo);
        }
        List<GameRoom> rooms = gameRoomRepository.findIsEnterAndSelectedLangAndLevelRooms(util.findServerName(langIdx, levelIdx));
        return new FindRoomsResult(userGameInfo, rooms);
    }

    // 방생성과 동시에 해당 난이도, 언어에 맞는 랜덤 문제 할당
    @Override
    @Transactional
    public GameRoom createRoom(int langIdx, int questionLevelIdx, User user) {
        log.info("{} 님 방 생성", user.getUserId());

        Language language = util.getSelectedLang(langIdx);
        QuestionLevel level = util.getSelectedLevel(questionLevelIdx);
        UserGameInfo userGameInfo = userRedisRepository.findUser(user.getUserId());

        if (userGameInfo == null) {
            userGameInfo = UserGameInfo.builder()
                    .playerName(user.getUserId())
                    .profileUrl(user.getAvatarUrl())
                    .winCnt(user.getWinCnt())
                    .loseCnt(user.getLoseCnt())
                    .build();
        }

        //방 생성
        GameRoom gameRoom = Optional.ofNullable(gameRoomRepository.createGameRoom(language, level, userGameInfo))
                .orElseThrow(() -> new NullPointerException("방 생성 오류 발생"));

        //세션 저장
        sessionRepository.saveEnterSession(gameRoom.getRoomId(), user.getUserId(), GameSessionRepository.CREATOR);
        //사용자 이름으로 방 찾기에 필요한 데이터 저장
        sessionRepository.saveRoomIdBySession(user.getUserId(), gameRoom.getRoomId());
        return gameRoom;
    }

    //입장 가능 확인
    @Override
    public boolean isEnter(String server, String roomId) {
        if (server == null || roomId == null) throw new NullPointerException("선택된 방이 없습니다.");
        GameRoom room = Optional.ofNullable(gameRoomRepository.findRoomById(server, roomId))
                .orElseThrow(() -> new NullPointerException("해당 방을 찾을 수 없습니다."));
        return room.isEnter();
    }

    //방 입장, 본인 정보 전송, userDetailPrincipal 추가
    @Override
    public UserGameInfo enterRoom(EnterAndExitRoomRequestDto dto, User user) {

        log.info("방: {} 입장:{}", dto.getRoomId(), user.getUserId());
        GameRoom room = Optional.ofNullable(gameRoomRepository.findRoomById(dto.getServer(), dto.getRoomId()))
                .orElseThrow(() -> new NullPointerException("해당 방을 찾을 수 없습니다."));

        UserGameInfo creator = room.getCreatorGameInfo();

        gameRoomRepository.enterAndExitGameRoom(room, true);
        // 입장자 세션 저장
        sessionRepository.saveEnterSession(dto.getRoomId(), user.getUserId(), GameSessionRepository.PARTICIPANT);
        //연결 해제 시 유저 아이디로 방을 찾기 위해 데이터 저장
        sessionRepository.saveRoomIdBySession(user.getUserId(), dto.getRoomId());
        return creator;
    }


    // 방 퇴장 메서드
    @Override
    public void exitRoom(String server, String roomId, User user) {
        if (server != null || roomId != null || user != null) {
            String role = sessionRepository.getRole(roomId, user.getUserId());
            log.info("퇴장 이벤트 사용 :{}, ROLE:{}", user.getUserId(), role);
            GameRoom roomById = gameRoomRepository.findRoomById(server, roomId);
            exitEvent(roomById, user.getUserId(), role);
        }
    }

    // 연결 해제 시 해당 방 관련 데이터 삭제 메서드
    @Override
    public void disconnectEvent(String username) {
        if (username != null) {
            String roomId = sessionRepository.findRoomIdBySession(username);
            log.info("방 퇴장 처리 중:{}", roomId);
            String role = sessionRepository.getRole(roomId, username);
            String server = gameRoomRepository.findServer(roomId);
            GameRoom roomById = gameRoomRepository.findRoomById(server, roomId);
            exitEvent(roomById, username, role);
        }
    }

    // 퇴장 처리 메서드
    public void exitEvent(GameRoom room, String username, String role) {

        if (username != null && role != null) {
            //중간에 방 나갔을 때 준비 데이터 존재 시 삭제
            boolean existKey = readyMap.containsKey(ROOM_READY + room.getRoomId());

            if (existKey) {
                readyMap.remove(ROOM_READY + room.getRoomId());
            }

            //방 정보에서 입장 가능으로 변경
            gameRoomRepository.enterAndExitGameRoom(room, false);
            // 방 기준 세션 정보 삭제
            sessionRepository.deleteSession(room.getRoomId(), username);
            //세션에 저장된 방 정보 삭제
            sessionRepository.deleteRoomIdBySession(username);
            Long cnt = sessionRepository.roomEnterCnt(room.getRoomId());
            log.info("잔여 인원:{}", cnt);
            //방장 일 때 처리
            if (role.equals(GameSessionRepository.CREATOR)) {

                if (cnt == 1) {

                    log.info("방 참여자 권한 상승 진행");
                    String othersSession = sessionRepository.findOthersSession(room.getRoomId(), username);
                    UserGameInfo otherUserGameInfo = userRedisRepository.findUser(othersSession);

                    if (otherUserGameInfo == null) {
                        Optional<User> byUserId = userRepository.findByUserId(othersSession);
                        User otherUser = byUserId.orElseThrow(() -> new NullPointerException("없는 사용자 입니다."));
                        otherUserGameInfo = userToUserInfo(otherUser);
                    }

                    //참여자 -> 방장 권한 상승
                    sessionRepository.upgradeRole(room.getRoomId(), othersSession);
                    // redis 방 정보 방장 데이터 교환
                    gameRoomRepository.changeCreator(room, otherUserGameInfo);

                } else if (cnt == 0) {

                    log.info("방 삭제 로직 진행");
                    gameRoomRepository.deleteRoom(room.getServer(), room.getRoomId());
                    gameRoomRepository.deleteServerRoomData(room.getRoomId());

                }
            }else{
                updateGameRoom(room.getRoomId());
            }

        }

    }

    public void updateGameRoom(String roomId) {
        Long cnt = sessionRepository.roomEnterCnt(roomId);
        if(cnt ==1){
            String server = gameRoomRepository.findServer(roomId);
            GameRoom roomById = gameRoomRepository.findRoomById(server, roomId);
            String playerName = roomById.getCreatorGameInfo().getPlayerName();
            UserGameInfo userInfo = userRedisRepository.findUser(playerName);
            gameRoomRepository.changeCreator(roomById,userInfo);
            log.info("방 정보 갱신 완료, server:{},creator:{},userInfo:{}",server,playerName,userInfo.toString());
        }


    }

    //준비 메시지 처리
    @Override
    public void ready(ReadyMessage message) {

        if (!readyMap.containsKey(ROOM_READY + message.getRoomId())) {
            readyMap.put(ROOM_READY + message.getRoomId(), 1);
        } else {
            GameRoom room = gameRoomRepository.findRoomById(message.getServer(), message.getRoomId());
            QuestionRedis question = randomQuestions(util.getQuestionLevel(room.getQuestionLevel()));
            message.setQuestionId(question.getId());
            message.setTitle(question.getTitle());
            message.setQuestion(question.getQuestion());
            message.setTemplate(question.getTemplates().get(util.getStartTemplateKey(room.getLanguage())));

            //준비 데이터 삭제
            redisTemplate.convertAndSend(channelTopic.getTopic(), message);
            readyMap.remove(ROOM_READY + message.getRoomId());
        }
    }

    @Override
    public Boolean isParticipant(String roomId, String username) {
        try {
            String role = sessionRepository.getRole(roomId, username);
            return role.equals(GameSessionRepository.PARTICIPANT);
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public void sendGameCode(GameMessage message) {

        String othersSession = sessionRepository.findOthersSession(message.getRoomId(), message.getSender());
        if (othersSession != null) {
            log.info("sender:{}", message.getSender());
            log.info("to:{}", othersSession);
            message.setTo(othersSession);
            redisTemplate.convertAndSend(channelTopic.getTopic(), message);
        }
    }

    @Override
    public void sendToMyUserInfo(String roomId, String username) {
        log.info("roomId: {}", roomId);
        log.info("username: {}", username);

        UserGameInfo userInfo = userRedisRepository.findUser(username);
        if (userInfo == null) {
            Optional<User> byUserId = userRepository.findByUserId(username);
            User myInfo = byUserId.orElseThrow(() -> new NullPointerException("유저 정보가 없습니다."));
            userInfo = userToUserInfo(myInfo);
        }

        String othersSession = sessionRepository.findOthersSession(roomId, username);
        UserAndRoomIdDto userAndRoomIdDto = new UserAndRoomIdDto(roomId, userInfo, othersSession);
        redisTemplate.convertAndSend(channelTopic.getTopic(), userAndRoomIdDto);

    }


    @Transactional(readOnly = true)
    public QuestionRedis randomQuestions(QuestionLevel level) {
        try {
            QuestionsByLevel easy1 = questionsIdListByLevelRepository.findById(level.name())
                    .orElseThrow(() -> new NullPointerException("데이터 없음"));

            Collections.shuffle(easy1.getQuestions());
            Long questionId = easy1.getQuestions().get(0);
            return questionsRedisRepository.findById(questionId).orElseThrow(() -> new NullPointerException("캐시 데이터 없음"));

        } catch (NullPointerException e) {
            List<Long> idListByLevel = questionDslRepository.findIdListByLevel(level);

            if (level.equals(QuestionLevel.HARD)) {
                idListByLevel.remove(idListByLevel.size() - 1);
            }
            Collections.shuffle(idListByLevel);

            Question oneQuestionByLevel = questionDslRepository.findOneQuestionByLevel(level, idListByLevel.get(0));
            QuestionRedis redis = new QuestionRedis();
            BeanUtils.copyProperties(oneQuestionByLevel, redis);

            List<Long> casesIds = new ArrayList<>();
            for (TestCase aCase : oneQuestionByLevel.getCases()) {
                casesIds.add(aCase.getId());
            }
            redis.setCasesIds(casesIds);
            QuestionsByLevel questionsByLevel = new QuestionsByLevel();
            questionsByLevel.setId(level.name());
            questionsByLevel.add(oneQuestionByLevel.getId());
            questionsIdListByLevelRepository.save(questionsByLevel);
            return questionsRedisRepository.save(redis);
        }

    }

    @Override
    public UserGameInfo userToUserInfo(User user) {
        return UserGameInfo.builder()
                .playerName(user.getUserId())
                .profileUrl(user.getAvatarUrl())
                .winCnt(user.getWinCnt())
                .loseCnt(user.getLoseCnt())
                .build();
    }
}
