package com.vivvo.userservice.core;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@ConfigurationProperties(prefix = "mailgun.api")
@Setter
@Getter
public class MailgunConfigurations {
    private String baseUrl;
    private String messagesUrl;
    private String username;
    private String key;
}
