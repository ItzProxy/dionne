package com.vivvo.userservice.core.Email;

import com.vivvo.userservice.EmailDto;
import com.vivvo.userservice.UserDto;
import com.vivvo.userservice.core.User.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class EmailValidator {

    private final EmailRepository emailRepository;

    @Autowired
    public EmailValidator(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    public Map<String, String> validate(EmailDto emailDto) {
        boolean isCreate = emailDto.getUserId() == null;
        Map<String, String> validationErrors = new LinkedHashMap<>();
        return validationErrors;
    }

}
