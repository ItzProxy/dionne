package com.vivvo.userservice;


import lombok.Setter;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import net.sargue.mailgun.Response;
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

    public UserDto update(UUID userId, UserDto dto) {
        return userTarget()
                .path(userId.toString())
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

    public UserDto findUserByUserId(UUID userId){
        return userTarget()
                .path(userId.toString())
                .request()
                .get(new GenericType<UserDto>(){});
    }


    private WebTarget userTarget() {
        return ClientBuilder.newClient()
                .target(baseUri)
                .path("api")
                .path("v1")
                .path("users");

    }

    public EmailDto findEmailByEmailId(UUID userId, UUID emailId){
        return emailTarget(userId)
                .path(emailId.toString())
                .request()
                .get(new GenericType<EmailDto>(){});
    }

    public EmailDto createEmail(UUID userId,EmailDto dto) {
        return emailTarget(userId)
                .request()
                .post(Entity.json(dto), EmailDto.class);
    }

    public EmailDto updateEmail(UUID userId, EmailDto dto) {
        return emailTarget(userId)
                .path(dto.getEmailId().toString())
                .request()
                .put(Entity.json(dto), EmailDto.class);
    }

    public void deleteEmail(UUID userId, UUID emailId) {
        emailTarget(userId)
                .path(emailId.toString())
                .request()
                .delete(Void.class);
    }

    public List<EmailDto> findEmailsByUserId(UUID userId){
        return emailTarget(userId)
                .request()
                .get(new GenericType<List<EmailDto>>(){});
    }

    public EmailDto updatePrimaryEmailByEmailId(UUID userId, UUID emailId){
        return emailTarget(userId)
                .path(emailId.toString())
                .path("primary")
                .request()
                .post(Entity.json(new EmailDto()), EmailDto.class);
    }

    public Boolean sendEmailToPrimary(UUID userId){
        return emailTarget(userId)
                .path("sendEmail")
                .request()
                .post(Entity.json(null), Boolean.class);
    }

    public Boolean verifyEmail(UUID userId, UUID emailId, UUID verifyId){
        return emailTarget(userId)
                .path(emailId.toString())
                .path("verify")
                .path(verifyId.toString())
                .request()
                .post(Entity.json(null), Boolean.class);
    }

    public Boolean sendVerifyEmail(UUID userId, UUID emailId){
        return emailTarget(userId)
                .path(emailId.toString())
                .path("verify")
                .request()
                .post(Entity.json(null), Boolean.class);
    }

    private WebTarget emailTarget(UUID userId) {
        return ClientBuilder.newClient()
                .target(baseUri)
                .path("api")
                .path("v1")
                .path("users")
                .path(userId.toString())
                .path("emails");
    }

}
