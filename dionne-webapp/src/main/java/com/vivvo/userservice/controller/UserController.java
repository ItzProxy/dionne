package com.vivvo.userservice.controller;


import com.vivvo.userservice.UserClient;
import com.vivvo.userservice.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserClient userClient;

    @GetMapping
    public List<UserDto> findAllUsers() {
        return userClient.findAddUsers();
    }

    @GetMapping("/{userId}")
    public UserDto findOneUserByUserId(@PathVariable UUID userId){
        return userClient.findUserByUserId(userId);
    }

    @PutMapping("/{userId}")
    public UserDto updateUser(@PathVariable UUID userId, @RequestBody UserDto userDto){
        return userClient.update(userId, userDto);
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto){
        return userClient.create(userDto);
    }



}
