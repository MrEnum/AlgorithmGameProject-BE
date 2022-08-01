package com.seventeam.algoritmgameproject.web.controller;


import com.seventeam.algoritmgameproject.domain.model.game.GameMessage;
import com.seventeam.algoritmgameproject.domain.model.game.GameProcessMessage;
import com.seventeam.algoritmgameproject.domain.model.game.ReadyMessage;
import com.seventeam.algoritmgameproject.web.service.game_service.GameProcess;
import com.seventeam.algoritmgameproject.web.service.game_service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GameController {

    private final GameService service;
    private final GameProcess process;
    //특정 유저로 수정
    @MessageMapping("/game/codeMessage")
    public void getCodeMessage(GameMessage message) {
      service.sendGameCode(message);
    }

    @MessageMapping("/game/process")
    public void processManager(GameProcessMessage.Request request) {
        process.GameProcessManager(request);
    }

    @MessageMapping("/game/ready")
    public void ready(ReadyMessage message) {
        log.info("READY:{}", message.getType());
        service.ready(message);
    }
}
