package com.seventeam.algoritmgameproject.config;

import com.seventeam.algoritmgameproject.config.stomp.AgentWebSocketHandlerDecoratorFactory;
import com.seventeam.algoritmgameproject.config.stomp.CustomHandShaker;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker // stomp 사용 선언
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final AgentWebSocketHandlerDecoratorFactory decoratorFactory;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic","/queue")
                .setTaskScheduler(taskScheduler());
        config.setApplicationDestinationPrefixes("/app");
    }
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.initialize();
        return taskScheduler;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp").setAllowedOriginPatterns("*")
                .setAllowedOrigins("localhost:3000")// 소켓 연결 요청 URI
                .setAllowedOrigins("chinda.live")
                .setAllowedOrigins("www.chinda.live")
                .setHandshakeHandler(new CustomHandShaker())
                .withSockJS(); // sock.js를 통하여 낮은 버전의 브라우저에서도 websocket이 동작할수 있게 합니다.
    }
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setDecoratorFactories(decoratorFactory).setTimeToFirstMessage(3000000).setSendTimeLimit(300000); // Time
    }

}
