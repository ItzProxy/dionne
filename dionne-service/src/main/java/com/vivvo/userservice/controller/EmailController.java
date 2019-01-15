package com.vivvo.userservice.controller;

import com.vivvo.userservice.EmailDto;
import com.vivvo.userservice.core.Email.EmailService;
import lombok.extern.slf4j.Slf4j;
import net.sargue.mailgun.Response;
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

    @GetMapping("/{emailId}")
    public EmailDto findEmailByEmailId(@PathVariable UUID userId, @PathVariable UUID emailId){
        return emailService.findOneEmailByEmailId(userId, emailId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmailDto addEmail(@PathVariable UUID userId, @RequestBody EmailDto dto){
        return emailService.create(userId, dto);
    }

    @DeleteMapping("/{emailId}")
    public void deleteEmail(@PathVariable UUID userId, @PathVariable UUID emailId){
        emailService.delete(userId, emailId);
    }


    //TODO finish this shit
    @PostMapping("{emailId}/primary")
    public EmailDto changeEmailPrimary(@PathVariable UUID userId, @PathVariable UUID emailId){
        return emailService.makeEmailPrimaryByEmailId(userId, emailId);
    }

    //TODO mailgun
    @PostMapping("/sendEmail")
    public Response sendEmail(@PathVariable UUID userId){
        return emailService.sendEmail(userId,"Yep","it doesn't work ok.");
    }


    //TODO finish
}
