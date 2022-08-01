package com.seventeam.algoritmgameproject.web.service.game_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seventeam.algoritmgameproject.domain.model.game.GameProcessMessage;
import com.seventeam.algoritmgameproject.web.dto.game_dto.UserAndRoomIdDto;
import com.seventeam.algoritmgameproject.domain.model.game.GameMessage;
import com.seventeam.algoritmgameproject.domain.model.game.ReadyMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;


@Slf4j
@RequiredArgsConstructor
@Component
public class RedisSubscriber {
    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;
    private static final String READY = "READY";
    private static final String GAME = "GAME";
    private static final String USERINFO = "USERINFO";

    /**
     * Redis에서 메시지가 발행(publish)되면 대기하고 있던 onMessage가 해당 메시지를 받아 처리한다.
     */

    public void sendMessage(String publishMessage) {
        try {
            // redis에서 발행된 데이터를 받아 deserialize

            log.info("ONMESSAGE:{}", publishMessage);

            if (publishMessage == null) {
                throw new RuntimeException("메시지 오류");
            }

            if (publishMessage.contains(READY)) {

                ReadyMessage roomMessage = objectMapper.readValue(publishMessage, ReadyMessage.class);
                send(roomMessage.getRoomId(), roomMessage);

            } else if (publishMessage.contains(GAME)) {

                GameMessage roomMessage = objectMapper.readValue(publishMessage, GameMessage.class);
                sendToUser(roomMessage.getTo(),roomMessage.getRoomId(),roomMessage);

            } else if (publishMessage.contains(USERINFO)) {

                UserAndRoomIdDto dto = objectMapper.readValue(publishMessage, UserAndRoomIdDto.class);
                sendToUser(dto.getTo(),dto.getRoomId(),dto.getInfo());

            }else{
                GameProcessMessage.Request request = objectMapper.readValue(publishMessage, GameProcessMessage.Request.class);
                GameProcessMessage.Response response = new GameProcessMessage.Response(request.getType());
                sendToUser(request.getTo(),request.getRoomId(),response);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    //구독한 사람에게 send
    public void send(String roomId, Object roomMessage) {
        if(roomId!=null){
            messagingTemplate.convertAndSend("/topic/game/room/" + roomId, roomMessage);
        }
    }


    //특정사용자에게 send
    public void sendToUser(String to, String roomId, Object o) {
        log.info("SEND:{}", to);
        if(to!=null){
            messagingTemplate.convertAndSendToUser(to, "/queue/game/codeMessage/" + roomId, o);
        }
    }
}
