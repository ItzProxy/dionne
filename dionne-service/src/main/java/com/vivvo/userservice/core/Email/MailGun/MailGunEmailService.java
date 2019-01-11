package com.vivvo.userservice.core.Email.MailGun;

import com.vivvo.userservice.core.MailgunConfigurations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailGunEmailService implements EmailService {

    @Autowired
    private MailgunConfigurations mailgunConfigurations;

    @Override
    public void sendHTML(String from, String to, String subject, String body) {

    }

    @Override
    public void sendText(String from, String to, String subject, String body) {

    }
}
