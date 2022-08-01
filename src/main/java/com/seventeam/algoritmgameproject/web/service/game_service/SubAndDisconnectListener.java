package com.seventeam.algoritmgameproject.web.service.game_service;

import com.seventeam.algoritmgameproject.web.repository.game_repository.GameSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubAndDisconnectListener implements ApplicationListener<AbstractSubProtocolEvent> {

    private final GameService service;
    private final GameSessionRepository repository;

    @Override
    public void onApplicationEvent(AbstractSubProtocolEvent event) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        String destination = Optional.ofNullable(accessor.getDestination()).orElse("notFoundRoomId");
        String roomId = getRoomId(destination);
        String username = String.valueOf(accessor.getUser());

        if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            //유저 정보 전송 , 구독 주소 변경
            log.info("방:{},입장:{}", roomId, username);
            if (service.isParticipant(roomId, username) && destination.equals("/topic/game/room/" + roomId)) {
                log.info("Connect User: {}", username);
                service.sendToMyUserInfo(roomId, username);
            }
        } else if (StompCommand.DISCONNECT == accessor.getCommand()) {

            if (username != null && repository.notYetExit(username)) {
                log.info("퇴장:{}", username);
                log.info("Disconnect username: {}", username);
                service.disconnectEvent(username);
            }

        }
    }

    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1)
            return destination.substring(lastIndex + 1);
        else
            return "";
    }
}
