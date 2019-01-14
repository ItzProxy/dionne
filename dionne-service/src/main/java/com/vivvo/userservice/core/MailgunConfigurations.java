package com.vivvo.userservice.core;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mailgun.api")
@Setter
@Getter
public class MailgunConfigurations {
    private String baseurl;
    private String messagesurl;
    private String username;
    private String key;
    private String domainname;
}
