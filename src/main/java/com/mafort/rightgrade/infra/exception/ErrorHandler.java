package com.mafort.rightgrade.infra.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ErrorHandler {
    @Autowired
    private MessageSource messageSource;

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

    /**
     * Exception handler for invalid code.
     *
     * @param invalidCodeException The InvalidCodeException thrown
     * @return ResponseEntity containing the error message and HTTP status code 401 (Unauthorized).
     */
    @ExceptionHandler(InvalidCodeException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCode(InvalidCodeException invalidCodeException){
        Map<String, String> errors = new HashMap<>();
        errors.put("error", invalidCodeException.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errors);
    }


    /** Exception handler for invalid argument.
     *
     * @param invalidArgumentException The InvalidArgumentException thrown
     * @return ResponseEntity containing the error message and HTTP status code 400.
     */
    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<Map<String, String>> handleInvalidArgument(InvalidArgumentException invalidArgumentException){
        Map<String, String> errors = new HashMap<>();
        errors.put("error", invalidArgumentException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * Exception handler for bad credentials during authentication.
     *
     * @return ResponseEntity containing the localized error message and HTTP status code 401 (Unauthorized).
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials() {
        Map<String, String> errors = new HashMap<>();
        errors.put("error",  messageSource.getMessage("error.authentication.badCredentials", null, LocaleContextHolder.getLocale()));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errors);
    }

    @ExceptionHandler(InvalidEmail.class)
    public ResponseEntity<Map<String, String>> handleInvalidEmail(){
        String message = messageSource.getMessage("error.invalid.email", null, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", message));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUsernameNotFound(UsernameNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", this.messageSource.getMessage("error.user.notFound", null, LocaleContextHolder.getLocale()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }
}
