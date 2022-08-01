package com.seventeam.algoritmgameproject.web.exception;

import com.seventeam.algoritmgameproject.web.controller.GameController;
import com.seventeam.algoritmgameproject.web.controller.GameRoomController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice(assignableTypes = {GameRoomController.class, GameController.class})
public class GameException {

    //조회 오류
    @ExceptionHandler(value = {NullPointerException.class})
    public ResponseEntity<Object> gameRoomFindException(NullPointerException ex) {
        log.error(ex.getMessage(), ex.getCause());
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
    @MessageExceptionHandler(value = {Exception.class})
    public void gameException(NullPointerException ex) {
        log.info("메시지 exception check:{}",ex.getMessage());
        log.error(ex.getMessage(), ex.getCause());
        //메세징 익셉션 처리
    }

}
