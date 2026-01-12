package com.vr.authservice.handler;

import com.vr.authservice.exception.UserAlreadyPresentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(exception = {MethodArgumentNotValidException.class})
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex
                .getFieldErrors()
                .stream()
                .collect(Collectors
                        .toMap(FieldError::getField, FieldError::getDefaultMessage, (s, s2) -> String.join(", ", s, s2))
                );
        return ResponseEntity
                .badRequest()
                .body(errors);
    }

    @ExceptionHandler(exception = {UsernameNotFoundException.class})
    public ResponseEntity<?> usernameNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(exception = {UserAlreadyPresentException.class})
    public ResponseEntity<?> userAlreadyPresentException(UserAlreadyPresentException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

}
