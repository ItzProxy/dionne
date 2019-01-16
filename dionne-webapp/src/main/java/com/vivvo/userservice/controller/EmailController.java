package com.vivvo.userservice.controller;

import com.vivvo.userservice.EmailDto;

import com.vivvo.userservice.UserClient;
import net.sargue.mailgun.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users/{userId}/emails")
public class EmailController {

    @Autowired
    private UserClient emailClient;

    @GetMapping
    public List<EmailDto> findAllEmails(@PathVariable UUID userId) {
        return emailClient.findEmailsByUserId(userId);
    }

    @GetMapping("/{emailId}")
    public EmailDto findEmailByEmailId(@PathVariable UUID userId, @PathVariable UUID emailId){
        return emailClient.findEmailByEmailId(userId, emailId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmailDto addEmail(@PathVariable UUID userId, @RequestBody EmailDto dto){
        return emailClient.createEmail(userId, dto);
    }

    @DeleteMapping("/{emailId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmail(@PathVariable UUID userId, @PathVariable UUID emailId){
        emailClient.deleteEmail(userId, emailId);
    }


    //TODO finish this shit
    @PostMapping("{emailId}/primary")
    public EmailDto changeEmailPrimary(@PathVariable UUID userId, @PathVariable UUID emailId){
        return emailClient.updatePrimaryEmailByEmailId(userId, emailId);
    }

    //TODO mailgun for the email services in mybff
    @PostMapping("/sendEmail")
    public Boolean sendEmail(@PathVariable UUID userId){
        return emailClient.sendEmailToPrimary(userId);
    }


    @GetMapping("/{emailId}/verify/{verifyId}")
    public Boolean verifyEmail(@PathVariable UUID userId, @PathVariable UUID emailId, @PathVariable UUID verifyId){
        return emailClient.verifyEmail(userId,emailId,verifyId);
    }


    //TODO verification
    @PostMapping("/{emailId}/verify")
    public Boolean sendVerificationEmail(@PathVariable UUID userId, @PathVariable UUID emailId){
        return emailClient.sendVerifyEmail(userId,emailId);
    }
}
