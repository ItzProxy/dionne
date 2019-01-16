package com.vivvo.userservice.core;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@ConfigurationProperties(prefix = "ngrok.endpoint")
@Setter
@Getter
public class NgrokConfiguration {
    private URI url;
}
