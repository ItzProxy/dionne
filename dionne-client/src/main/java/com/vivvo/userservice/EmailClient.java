package com.vivvo.userservice;


import lombok.Setter;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.util.List;
import java.util.UUID;

public class EmailClient {

    @Setter
    private String baseUri = "http://localhost:4444";

    public EmailDto create(EmailDto dto) {
        return emailTarget()
                .request()
                .post(Entity.json(dto), EmailDto.class);
    }

    public EmailDto update(EmailDto dto) {
        return emailTarget()
                .path(dto.getEmailId().toString())
                .request()
                .put(Entity.json(dto), EmailDto.class);
    }

    public void delete(UUID emailId) {
        emailTarget()
                .path(emailId.toString())
                .request()
                .delete(Void.class);
    }

    public List<EmailDto> findAllEmails() {
        return emailTarget()
                .request()
                .get(new GenericType<List<EmailDto>>(){});
    }

    public List<EmailDto> getEmailsByUserId(UUID userId){
        return emailTarget()
                .path(userId.toString())
                .request()
                .get(new GenericType<List<EmailDto>>(){});
    }

    public EmailDto changeEmailPrimaryByEmailId(UUID userId, UUID emailId){
        return emailTarget()
                .path(userId.toString())
                .path(emailId.toString())
                .path("primary")
                .request()
                .get(new GenericType<EmailDto>(){});
    }

    private WebTarget emailTarget() {
        return ClientBuilder.newClient()
                .target(baseUri)
                .path("api")
                .path("v1")
                .path("users")
                .path("email");

    }
}
