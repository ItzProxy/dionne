package com.vivvo.userservice.core.Email.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmailUserIdNoMatchException extends RuntimeException {

    public EmailUserIdNoMatchException(UUID userId, UUID emailId) {
        super("Provided email: emailId - " + emailId.toString()
        + " does match the provided an emailId associated with the userId: " + userId.toString());
    }
}
