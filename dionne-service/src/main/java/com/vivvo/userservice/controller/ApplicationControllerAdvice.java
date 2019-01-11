package com.vivvo.userservice.controller;

import com.vivvo.userservice.core.Email.EmailNotFoundException;
import com.vivvo.userservice.core.Email.EmailUserIdNoMatchException;
import com.vivvo.userservice.core.User.UserNotFoundException;
import com.vivvo.userservice.core.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class ApplicationControllerAdvice {

    @Autowired
    private MessageSourceAccessor messageSourceAccessor;

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException exception, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("message", exception.getMessage()));
    }

    @ExceptionHandler(value = EmailNotFoundException.class)
    public ResponseEntity<Object> handleEmailNotFoundException(EmailNotFoundException exception, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("message", exception.getMessage()));
    }

    @ExceptionHandler(value = EmailUserIdNoMatchException.class)
    public ResponseEntity<Object> handleEmailUserIdNoMatchException(EmailUserIdNoMatchException exception, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Collections.singletonMap("message", exception.getMessage()));
    }


    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<Object> handleUserNotFoundException(ValidationException exception, WebRequest request) {
        Map<String, String> translatedValidationErrors = exception.getValidationErrors()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> translate(e.getValue())));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(translatedValidationErrors);
    }

     private String translate(String key) {
        try {
            return messageSourceAccessor.getMessage(key, LocaleContextHolder.getLocale());
        }catch(NoSuchMessageException e) {
            log.warn("No translation found for key " + key);
            return key;
        }
     }
}


