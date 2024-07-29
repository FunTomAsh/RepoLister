package com.example.RepoLister.repositories;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ControllerAdvice
public class GlobalControllerExceptionHandler extends RuntimeException {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(Map.of(
                "status", HttpStatus.NOT_FOUND.value(),
                "message", ex.getMessage()
        ), HttpStatus.NOT_FOUND);
    }

    /*    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason="User doesn't exists.")  // 404
    @ExceptionHandler(UserNotFoundException.class)
    public void handleConflict() {
        // Nothing
    }*/
}