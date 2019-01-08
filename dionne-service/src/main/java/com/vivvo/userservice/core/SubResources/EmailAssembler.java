package com.vivvo.userservice.core.SubResources;

import com.vivvo.userservice.EmailDto;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EmailAssembler {

    public EmailDto assemble(Email email) {
        return new EmailDto()
                .setEmailId(email.getEmailId())
                .setUserId(email.getUserId())
                .setEmail(email.getEmail())
                .setIsPrimary(email.getIsPrimary());
    }

    public Email disassemble(EmailDto dto) {
        UUID emailId = dto.getEmailId() == null ? UUID.randomUUID() : dto.getEmailId();
        return new Email()
                .setEmailId(emailId)
                .setUserId(dto.getUserId())
                .setEmail(dto.getEmail())
                .setIsPrimary(dto.getIsPrimary());
    }

}
