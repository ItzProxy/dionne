package com.vivvo.userservice.controller;

import com.vivvo.userservice.EmailDto;
import com.vivvo.userservice.core.Email.EmailService;
import com.vivvo.userservice.core.Phone.PhoneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/users/{userId}/phones")
public class PhoneController {

    @Autowired
    private PhoneService phoneService;

    //TODO 
}
