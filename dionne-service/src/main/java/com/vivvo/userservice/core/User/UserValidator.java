package com.vivvo.userservice.core.User;

import com.vivvo.userservice.UserDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class UserValidator {

    private final UserRepository userRepository;

    @Autowired
    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        return validationErrors;
    }

    public Map<String, String> validateUpdate(UUID userId, UserDto userDto){
        Map<String, String> validationErrors = validate(userDto);
        if(!userId.equals(userDto.getUserId())){
            validationErrors.put("userId", "UserId param does not match the userId in request");
        }
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
