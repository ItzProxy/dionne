package com.vivvo.userservice.core;

import com.vivvo.userservice.UserDto;
import com.vivvo.userservice.UserEmailDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
public class EmailAssembler {


    public UserEmailDto assemble(Email userEmail) {
        return new UserEmailDto()
                .setUserID(userEmail.getUserId())
                .setEmailID(userEmail.getEmailId())
                .setEmail(userEmail.getEmail())
                .setBIsPrimary(userEmail.getBIsPrimary());
    }

    public Email disassemble(UserEmailDto dto) {
        UUID emailId = dto.getEmailID() == null ? UUID.randomUUID() : dto.getEmailID();
        return new Email()
                .setUserId(dto.getUserID())
                .setEmailId(emailId)
                .setEmail(dto.getEmail())
                .setBIsPrimary(dto.getBIsPrimary());
    }
}
