package com.vivvo.userservice.core.Email;

import com.vivvo.userservice.EmailDto;
import com.vivvo.userservice.core.User.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class EmailValidator {

    private final EmailRepository emailRepository;
    private final UserRepository userRepository;
    private EmailAddressValidator emailAddressValidator;

    @Autowired
    public EmailValidator(EmailRepository emailRepository, UserRepository userRepository) {
        this.emailRepository = emailRepository;
        this.userRepository = userRepository;
    }


    public Map<String, String> validate(UUID userId, EmailDto emailDto) {
        Map<String, String> validationErrors = new LinkedHashMap<>();

        if (StringUtils.isBlank(emailDto.getEmailAddress())) {
            validationErrors.put("emailAddress", "Email address is required.");
        } else if (emailDto.getEmailAddress().length() > 255) {
            validationErrors.put("emailAddress", "Email address must be 255 characters or less.");
            /*
        } else if(emailAddressValidator.getInstance().isValidEmail(emailDto.getEmailAddress())){
            validationErrors.put("emailAddress", "Email address is not in a valid form...I think");
            */
        } else if(!userId.equals(emailDto.getUserId())) {
            validationErrors.put("userId", "UserId provided does not match");
        }

        return validationErrors;
    }

}
