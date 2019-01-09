package com.vivvo.userservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vivvo.userservice.core.Email.EmailAssembler;
import com.vivvo.userservice.core.Email.EmailNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StreamUtils;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ClientErrorException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:teardown.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class EmailControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

    private EmailClient emailClient;
    private UserClient userClient;

    @Before
    public void before() {
        emailClient = new EmailClient();
        userClient = new UserClient();
        emailClient.setBaseUri("http://localhost:" + port);
        userClient.setBaseUri("http://localhost:" + port);
    }

    @Test
    public void testCreateAndSearch_shouldSucceed() {
        UserDto userDto = validUser();
        UserDto returnedUser = userClient.create(userDto);
        EmailDto emailDto = validEmail(returnedUser.getUserId());

        testRestTemplate.postForLocation("/api/v1/users/email" , emailDto);

        EmailDto[] emails = testRestTemplate.getForObject("/api/v1/users/email", EmailDto[].class);
        assertEquals(1, emails.length);
        assertEquals("testemail@test.com", emails[0].getEmail());
    }

    @Test
    public void testCreateAndSearchWithClient_shouldSucceed() {
        UserDto userDto = validUser();
        UserDto returnedUserDto = userClient.create(userDto);


        EmailDto emailDto = validEmail(returnedUserDto.getUserId());
        emailClient.create(emailDto);
        List<EmailDto> emails = emailClient.findAllEmails();

        assertEquals(1, emails.size());
        assertEquals("testemail@test.com", emails.get(0).getEmail());
    }

    @Test(expected = BadRequestException.class)
    public void testCreateWithNullUserId_shouldError(){
        UserDto userDto = validUser();
        UserDto returnedUserDto = userClient.create(userDto);


        EmailDto emailDto = validEmail(returnedUserDto.getUserId());
        emailDto.setUserId(null);

        emailClient.create(emailDto);
    }
    @Test(expected = BadRequestException.class)
    public void testCreateWithInvalidUserId_shouldError(){
        UserDto userDto = validUser();
        UserDto returnedUserDto = userClient.create(userDto);


        EmailDto emailDto = validEmail(returnedUserDto.getUserId());
        emailDto.setUserId(UUID.randomUUID());

        emailClient.create(emailDto);
    }

    @Test
    public void testCreateAndGetByUserId_shouldSucceed(){
        UserDto userDto = validUser();
        UserDto returnedUserDto = userClient.create(userDto);


        EmailDto emailDto = validEmail(returnedUserDto.getUserId());
        emailClient.create(emailDto);

        List<EmailDto> emails = emailClient.getEmailsByUserId(emailDto.getUserId());
        assertEquals(1, emails.size());
        assertEquals("testemail@test.com", emails.get(0).getEmail());
    }
    @Test
    public void testCreateAndDeleteWithValidUserId_shouldSuccess(){
        UserDto userDto = validUser();
        UserDto returnedUserDto = userClient.create(userDto);


        EmailDto emailDto = validEmail(returnedUserDto.getUserId());
        List<EmailDto> emails = new LinkedList<>();
        emails.add(emailClient.create(emailDto));
        emails.add(emailClient.create(emailDto));

        EmailDto changed = emailClient.changeEmailPrimaryByEmailId(emails.get(0).getUserId(), emails.get(0).getEmailId());
        assertEquals(changed.getIsPrimary(), true);
    }

    @Test
    public void testCreateAndSetPrimaryForOneTrue_shouldSucceed(){
        UserDto userDto = validUser();
        UserDto returnedUserDto = userClient.create(userDto);


        EmailDto emailDto = validEmail(returnedUserDto.getUserId());
    }

    @Test(expected = EmailNotFoundException.class)
    public void testInvalidEmailId(){

    }

    private EmailDto validEmail(UUID userId) {
        return new EmailDto()
                .setUserId(userId == null ? UUID.randomUUID() : userId)
                .setEmail("testemail@test.com")
                .setIsPrimary(false);
    }

    private UserDto validUser() {
        return new UserDto()
                .setLastName("TestLastName")
                .setFirstName("TestFirstName")
                .setUsername("TestUsername");
    }


    private Map<String, String> unwrapClientException(ClientErrorException exception) {
        try {
            InputStream inputStream = (InputStream) exception.getResponse().getEntity();
            String string = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            return new ObjectMapper().readValue(string, new TypeReference<Map<String, String>>(){});
        }catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
