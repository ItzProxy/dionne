package com.vivvo.userservice;


import lombok.Setter;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.util.List;
import java.util.UUID;

public class UserClient {

    @Setter
    private String baseUri = "http://localhost:4444";

    public UserDto create(UserDto dto) {
        return userTarget()
                .request()
                .post(Entity.json(dto), UserDto.class);
    }

    public UserDto update(UserDto dto) {
        return userTarget()
                .path(dto.getUserId().toString())
                .request()
                .put(Entity.json(dto), UserDto.class);
    }

    public void delete(UUID userId) {
        userTarget()
                .path(userId.toString())
                .request()
                .delete(Void.class);
    }

    public List<UserDto> findAddUsers() {
        return userTarget()
                .request()
                .get(new GenericType<List<UserDto>>(){});
    }



    private WebTarget userTarget() {
        return ClientBuilder.newClient()
                .target(baseUri)
                .path("api")
                .path("v1")
                .path("users");

    }
}
