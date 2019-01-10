package com.vivvo.userservice.core.Email;

import com.vivvo.userservice.EmailDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailAssembler {

    public EmailDto assemble(Email email) {
        //log.warn(emailAddress.getEmailId().toString() + emailAddress.getUserId().toString() + emailAddress.getEmailAddress());
        return new EmailDto()
                .setEmailId(email.getEmailId())
                .setUserId(email.getUserId())
                .setEmailAddress(StringUtils.strip(email.getEmailAddress()))
                .setIsPrimary(email.getIsPrimary());
    }

    public Email disassemble(EmailDto dto) {
        return new Email()
                .setEmailId(dto.getEmailId())
                .setUserId(dto.getUserId())
                .setEmailAddress(dto.getEmailAddress())
                .setIsPrimary(dto.getIsPrimary());
    }

}
