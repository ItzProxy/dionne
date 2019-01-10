package com.vivvo.userservice;

import com.vivvo.userservice.core.User.UserRepository;
import com.vivvo.userservice.core.User.UserValidator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.UUID;

public class UserValidatorTest {

    private UserValidator userValidator;

    @Before
    public void before() {
        UserRepository mockUserRepository = mock(UserRepository.class);
        when(mockUserRepository.existsByUsername(anyString())).thenReturn(false);
        userValidator = new UserValidator(mockUserRepository);
    }

    @Test
    public void testValidateWithNullFirstName_shouldReturnErrors() {
        UserDto dto = newValidUserForUpdate();
        dto.setFirstName(null);

        Map<String, String> validationErrors = userValidator.validate(dto);
        assertEquals(1, validationErrors.size());
        assertNotNull(validationErrors.get("firstName"));
    }

    @Test
    public void testValidateWithNullLastName_shouldReturnErrors() {
        UserDto dto = newValidUserForUpdate();
        dto.setLastName(null);

        Map<String, String> validationErrors = userValidator.validate(dto);
        assertEquals(1, validationErrors.size());
        assertNotNull(validationErrors.get("lastName"));
    }

    @Test
    public void testValidateWithLongFirstAndLastNames_shouldReturnErrors() {
        UserDto dto = newValidUserForUpdate();
        dto.setFirstName(RandomStringUtils.randomAlphabetic(35));
        dto.setLastName(RandomStringUtils.randomAlphabetic(35));

        Map<String, String> validationErrors = userValidator.validate(dto);
        assertEquals(2, validationErrors.size());
        assertNotNull(validationErrors.get("firstName"));
        assertNotNull(validationErrors.get("lastName"));
    }

    @Test
    public void testValidateWithNullUsername_shouldReturnErrors() {
        UserDto dto = newValidUserForUpdate();
        dto.setUsername(null);

        Map<String, String> validationErrors = userValidator.validate(dto);
        assertEquals(1, validationErrors.size());
        assertNotNull(validationErrors.get("username"));
    }

    @Test
    public void testValidateWithLongUsername_shouldReturnErrors() {
        UserDto dto = newValidUserForUpdate();
        dto.setUsername(RandomStringUtils.randomAlphabetic(256));

        Map<String, String> validationErrors = userValidator.validate(dto);
        assertEquals(1, validationErrors.size());
        assertNotNull(validationErrors.get("username"));
    }


    @Test
    public void testValidateWithDuplicateUsername_shouldReturnErrors() {
        UserDto dto = newValidUserForCreate();

        UserRepository mockUserRepository = mock(UserRepository.class);
        when(mockUserRepository.existsByUsername(anyString())).thenReturn(true);
        userValidator = new UserValidator(mockUserRepository);

        Map<String, String> validationErrors = userValidator.validate(dto);
        assertEquals(1, validationErrors.size());
        assertNotNull(validationErrors.get("username"));
    }


    private UserDto newValidUserForCreate() {
        return new UserDto()
                .setLastName("TestLastName")
                .setFirstName("TestFirstName")
                .setUsername("TestUserName");
    }

    private UserDto newValidUserForUpdate() {
        return newValidUserForCreate()
                .setUserId(UUID.randomUUID());
    }



}
