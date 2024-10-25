package com.mafort.rightgrade.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ControllerAdvice
public class ErrorHandler {
    /**
     * Exception handler that returns an error message and a status code
     * when the login credentials are incorrect.
     *
     * @return ResponseEntity containing the error message and HTTP status code 403 (FORBIDDEN).
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handle403error() {
        String message = "Invalid email or password";
        return ResponseEntity.status(401).body(message);
    }

}
