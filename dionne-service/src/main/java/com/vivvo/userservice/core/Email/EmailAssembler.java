package com.vivvo.userservice.core.Email;

import com.vivvo.userservice.EmailDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class EmailAssembler {

    public EmailDto assemble(Email email) {
        //log.warn(email.getEmailId().toString() + email.getUserId().toString() + email.getEmail());
        return new EmailDto()
                .setEmailId(email.getEmailId())
                .setUserId(email.getUserId())
                .setEmail(StringUtils.strip(email.getEmail()))
                .setIsPrimary(email.getIsPrimary());
    }

    public Email disassemble(EmailDto dto) {
        return new Email()
                .setEmailId(dto.getEmailId())
                .setUserId(dto.getUserId())
                .setEmail(dto.getEmail())
                .setIsPrimary(dto.getIsPrimary());
    }

}
