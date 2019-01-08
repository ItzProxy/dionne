package com.vivvo.userservice.controller;


import com.vivvo.userservice.EmailDto;
import com.vivvo.userservice.UserDto;
import com.vivvo.userservice.core.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.POST;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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

    @GetMapping("{userId}/email")
    public List<EmailDto> findEmailByUserId(@PathVariable UUID userId) {
        return userService.findEmailsByUserId(userId);
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


}
