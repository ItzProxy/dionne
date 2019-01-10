package com.vivvo.userservice.controller;

import com.vivvo.userservice.EmailDto;
import com.vivvo.userservice.core.Email.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/users/{userId}/emails")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping
    public List<EmailDto> findAllEmails(@PathVariable UUID userId) {
        return emailService.findAllEmails(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmailDto addEmail(@PathVariable UUID userId, @RequestBody EmailDto dto){
        return emailService.create(userId, dto);
    }



    //TODO finish this shit
    @PostMapping("/primary")
    public EmailDto changeEmailPrimary(@PathVariable UUID userId, @RequestParam UUID emailId){
        return emailService.makeEmailPrimaryByEmailId(userId, emailId);
    }
}
