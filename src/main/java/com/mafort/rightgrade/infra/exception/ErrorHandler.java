package com.mafort.rightgrade.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

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

    /**
     * Business rules exception handler, that capture user errors.
     * @return ResponseEntity containing a JSON with the respective errors and status code 400 (Bad request).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleBusinessRules(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Exception handler for invalid passwords.
     *
     * @param ex The InvalidPasswordException thrown
     * @return ResponseEntity containing the error message and HTTP status code 401 (Unauthorized).
     */
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Map<String, String>> handleInvalidPassword(InvalidPasswordException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errors);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String,String>> handleInvalidArgument(NotFoundException ex){
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

}
