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
@RequestMapping("/api/v1/users/email")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @GetMapping
    public List<EmailDto> findAllEmails() {
        return emailService.findAllEmails();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmailDto addEmailToUserId(@RequestBody EmailDto dto){
        return emailService.create(dto);
    }

    @GetMapping(params = "userId")
    public List<EmailDto> findEmailByUserId(@RequestParam UUID userId){
        return emailService.findEmailsByUserId(userId);
    }
}
