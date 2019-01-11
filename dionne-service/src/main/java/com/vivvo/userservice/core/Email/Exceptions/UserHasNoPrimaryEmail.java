package com.vivvo.userservice.core.Email.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserHasNoPrimaryEmail extends RuntimeException {
    public UserHasNoPrimaryEmail(UUID userId) {
        super("User does not have a primary email set: userId - " + userId.toString());
    }
}
