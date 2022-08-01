package com.seventeam.algoritmgameproject.config.stomp;

import com.sun.security.auth.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Slf4j
public class CustomHandShaker extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {

        String uri = request.getURI().toString();
        final String username = uri.split("=")[1];
        log.info("User with ID '{}' opened the page", username);

        //업그레이드 헤더에 username 설정,public static final String USER_HEADER = "simpUser";,key 등록, 세션을 키로 , principal name을 value로 등록해서 탐색
        return new UserPrincipal(username);

    }

    ;


}