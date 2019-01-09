package com.vivvo.userservice.controller;


import com.vivvo.userservice.EmailDto;
import com.vivvo.userservice.UserDto;
<<<<<<< HEAD
import com.vivvo.userservice.core.Email.EmailService;
import com.vivvo.userservice.core.User.UserService;
import lombok.extern.slf4j.Slf4j;
=======
import com.vivvo.userservice.UserEmailDto;
import com.vivvo.userservice.core.Email;
import com.vivvo.userservice.core.UserService;
>>>>>>> master
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping
    public List<UserDto> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping(params = "lastName")
    public List<UserDto> findUserByLastName(@RequestParam String lastName) {
        return userService.findUserByLastName(lastName);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody UserDto dto) {
        return userService.create(dto);
    }

    @PutMapping("/{userId}")
    public UserDto update(@PathVariable UUID userId, @RequestBody UserDto dto) {
        dto.setUserId(userId);
        return userService.update(dto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID userId){
        userService.delete(userId);
    }

    @GetMapping("/{username}/email")
    public List<UserEmailDto> getEmailByUserId(@PathVariable String username){
        return  userService.getAllEmailsByUsername(username);
    }
}
