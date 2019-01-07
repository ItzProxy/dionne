package com.vivvo.userservice.core;

import lombok.Getter;

import java.util.Map;

public class ValidationException extends RuntimeException{

    @Getter
    private Map<String, String> validationErrors;

    public ValidationException(Map<String, String> validationErrors){
        this.validationErrors = validationErrors;
    }

}
