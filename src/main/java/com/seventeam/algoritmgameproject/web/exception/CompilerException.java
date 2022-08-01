package com.seventeam.algoritmgameproject.web.exception;

import com.seventeam.algoritmgameproject.web.controller.CompilerController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice(assignableTypes = CompilerController.class)
public class CompilerException {
    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<Object> compilerException(RuntimeException ex) {
        log.error(ex.getMessage(), ex.getCause());
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.NOT_ACCEPTABLE
        );
    }
}
