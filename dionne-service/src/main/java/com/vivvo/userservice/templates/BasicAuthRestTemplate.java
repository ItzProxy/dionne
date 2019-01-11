package com.vivvo.userservice.templates;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Base64;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/*
copied from https://jonas.verhoelen.de/java/spring-resttemplate-basic-auth/
 */
public class BasicAuthRestTemplate extends RestTemplate {

    private String username;
    private String password;

    public BasicAuthRestTemplate(String username, String password) {
        super();
        this.username = username;
        this.password = password;
        addAuthentication();
    }

    private void addAuthentication() {
        if (StringUtils.isEmpty(username)) {
            throw new RuntimeException("Username is mandatory for Basic Auth");
        }
    }
}

