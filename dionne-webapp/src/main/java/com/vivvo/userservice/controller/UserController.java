package com.vivvo.userservice.controller;


import com.vivvo.userservice.UserClient;
import com.vivvo.userservice.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserClient userClient;

    @GetMapping
    public List<UserDto> findAllUsers() {
        return userClient.findAddUsers();
    }

}
