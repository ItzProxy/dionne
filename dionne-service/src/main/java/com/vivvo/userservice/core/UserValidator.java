package com.vivvo.userservice.core;

import com.vivvo.userservice.UserDto;
import org.apache.commons.lang3.StringUtils;
import org.hsqldb.lib.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class UserValidator {

    private final UserRepository userRepository;
    private final EmailRepository emailRepository;

    private UserEmailValidator emailValidator;

    @Autowired
    public UserValidator(UserRepository userRepository, EmailRepository emailRepository) {
        this.userRepository = userRepository;
        this.emailRepository = emailRepository;
    }


    public Map<String, String> validate(UserDto userDto) {
        boolean isCreate = userDto.getUserId() == null;
        Map<String, String> validationErrors = new LinkedHashMap<>();
        validateFirstName(userDto, validationErrors);

        if (StringUtils.isBlank(userDto.getLastName())) {
            validationErrors.put("lastName", "USER.VALIDATION.lastName.REQUIRED");
        } else if (userDto.getLastName().length() > 30) {
            validationErrors.put("lastName", "Last name must be 30 characters or less.");
        }

        if (StringUtils.isBlank(userDto.getUsername())) {
            validationErrors.put("username", "Username is required.");
        } else if (isCreate && userRepository.existsByUsername(userDto.getUsername())) {
            validationErrors.put("username", "This username already exists.");
        } else if (userDto.getUsername().length() > 255) {
            validationErrors.put("username", "Username must be 255 characters or less.");
        }
/*
        for (String email:
             userDto.getEmails()) {
            if (StringUtils.isBlank(email)) {
                validationErrors.put("email", "An Email is required.");
                break;
            }
            else if(!emailValidator.getInstance().isValidEmail(email)){
                validationErrors.put("email", "Email is invalid");
            }
        }
*/
        return validationErrors;
    }

    private void validateFirstName(UserDto userDto, Map<String, String> validationErrors) {
        if (StringUtils.isBlank(userDto.getFirstName())) {
            validationErrors.put("firstName", "A first name is required.");
        } else if (userDto.getFirstName().length() > 30) {
            validationErrors.put("firstName", "First name must be 30 characters or less.");
        }
    }

}
