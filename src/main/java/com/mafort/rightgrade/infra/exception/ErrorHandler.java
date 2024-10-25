package com.mafort.rightgrade.infra.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {
    /**
     * Exception handler that returns an error message and a status code
     * when the login are incorrect.
     *
     * @return ResponseEntity containing the error message and HTTP status code 401 (Unauthorized).
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handle403error() {
        String message = "Invalid email or password";
        return ResponseEntity.status(401).body(message);
    }

}
