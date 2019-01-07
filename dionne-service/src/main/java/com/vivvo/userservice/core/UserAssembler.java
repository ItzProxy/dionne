package com.vivvo.userservice.core;

import com.vivvo.userservice.UserDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserAssembler {

    public UserDto assemble(User user) {
        return new UserDto()
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setUserId(user.getUserId())
                .setUsername(user.getUsername());
    }

    public User disassemble(UserDto dto) {
        UUID userId = dto.getUserId() == null ? UUID.randomUUID() : dto.getUserId();
        return new User()
                .setUserId(userId)
                .setFirstName(StringUtils.trim(dto.getFirstName()))
                .setLastName(StringUtils.trim(dto.getLastName()))
                .setUsername(StringUtils.trim(dto.getUsername()));
    }

}
