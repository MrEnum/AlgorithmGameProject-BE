package com.seventeam.algoritmgameproject.web.exception;

import com.seventeam.algoritmgameproject.web.controller.OAuthController;
import com.seventeam.algoritmgameproject.web.dto.login_dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = OAuthController.class)
public class LoginException {
    // 변경
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleApiRequestException(Exception ex) {

        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

}
