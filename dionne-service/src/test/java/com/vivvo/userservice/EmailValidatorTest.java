package com.vivvo.userservice;

import com.vivvo.userservice.core.Email.EmailRepository;
import com.vivvo.userservice.core.Email.EmailValidator;
import com.vivvo.userservice.core.User.UserRepository;
import org.junit.Before;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class EmailValidatorTest {

    private EmailValidator emailValidator;

    @Before
    public void before() {
        EmailRepository mockEmailRepository = mock(EmailRepository.class);
        UserRepository mockUserRepository = mock(UserRepository.class);
        when(mockEmailRepository.existsByEmailAddress(anyString())).thenReturn(false);
        when(mockUserRepository.existsByUsername(anyString())).thenReturn(false);
        emailValidator = new EmailValidator(mockEmailRepository,mockUserRepository);
    }


    private EmailDto newValidEmailDto() {
        UUID aUUID = UUID.randomUUID();
        return new EmailDto()
                .setUserId(aUUID)
                .setEmailId(aUUID)
                .setEmailAddress("test@test.come")
                .setIsPrimary(true);
    }



}
