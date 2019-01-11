package com.vivvo.userservice.core.Email.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmailNotFoundException extends RuntimeException {

    public EmailNotFoundException(UUID emailId) {
        super("Email not found by id " + emailId.toString());
    }
}
