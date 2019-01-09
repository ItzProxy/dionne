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
    private final UserRepository userRepository;

    @Autowired
    public EmailValidator(EmailRepository emailRepository, UserRepository userRepository) {
        this.emailRepository = emailRepository;
        this.userRepository = userRepository;
    }


    public Map<String, String> validate(EmailDto emailDto) {
        Map<String, String> validationErrors = new LinkedHashMap<>();

        if (StringUtils.isBlank(emailDto.getEmail())) {
            validationErrors.put("email address", "Email address is required.");
        } else if (emailDto.getEmail().length() > 255) {
            validationErrors.put("email address", "Email address must be 255 characters or less.");
        } else if (emailDto.getUserId() == null || !userRepository.existsById(emailDto.getUserId())) {
            validationErrors.put("user id", "This user id does not exists.");
        }

        return validationErrors;
    }

}
