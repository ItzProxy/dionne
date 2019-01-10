package com.vivvo.userservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private UserClient userClient;

    @Before
    public void before() {

        userClient = new UserClient();
        userClient.setBaseUri("http://localhost:" + port);
    }

    @Test
    public void testCreateAndSearch_shouldSucceed() {
        UserDto userDto = validUser();
        UserDto returnedUser = userClient.create(userDto);
        EmailDto emailDto = validEmail(returnedUser.getUserId());

        testRestTemplate.postForLocation("/api/v1/users/"+returnedUser.getUserId().toString()+"/emails" , emailDto);

        EmailDto[] emails = testRestTemplate.getForObject("/api/v1/users/"+returnedUser.getUserId().toString()+"/emails", EmailDto[].class);
        assertEquals(1, emails.length);
        assertEquals("testemail@test.com", emails[0].getEmailAddress());
    }

    @Test
    public void testCreateAndSearchWithClient_shouldSucceed() {
        UserDto userDto = validUser();
        UserDto returnedUserDto = userClient.create(userDto);


        EmailDto emailDto = validEmail(returnedUserDto.getUserId());
        userClient.createEmail(emailDto.getUserId(), emailDto);

        List<EmailDto> emails = userClient.findEmailsByUserId(returnedUserDto.getUserId());

        assertEquals(1, emails.size());
        assertEquals("testemail@test.com", emails.get(0).getEmailAddress());
    }

    @Test(expected = BadRequestException.class)
    public void testCreateWithNullUserId_shouldError(){
        UserDto userDto = validUser();
        UserDto returnedUserDto = userClient.create(userDto);


        EmailDto emailDto = validEmail(returnedUserDto.getUserId());
        emailDto.setUserId(null);

        userClient.createEmail(returnedUserDto.getUserId(),emailDto);
    }
    @Test(expected = BadRequestException.class)
    public void testCreateWithInvalidUserId_shouldError(){
        UserDto userDto = validUser();
        UserDto returnedUserDto = userClient.create(userDto);


        EmailDto emailDto = validEmail(returnedUserDto.getUserId());
        emailDto.setUserId(UUID.randomUUID());

        userClient.createEmail(returnedUserDto.getUserId(), emailDto);
    }

    @Test
    public void testCreateAndGetByUserId_shouldSucceed(){
        UserDto userDto = validUser();
        UserDto returnedUserDto = userClient.create(userDto);


        EmailDto emailDto = validEmail(returnedUserDto.getUserId());
        userClient.createEmail(returnedUserDto.getUserId(),emailDto);

        List<EmailDto> emails = userClient.findEmailsByUserId(emailDto.getUserId());
        assertEquals(1, emails.size());
        assertEquals("testemail@test.com", emails.get(0).getEmailAddress());
    }
    @Test
    public void testCreateAndDeleteWithValidUserId_shouldSuccess(){
        UserDto userDto = validUser();
        UserDto returnedUserDto = userClient.create(userDto);


        EmailDto emailDto = validEmail(returnedUserDto.getUserId());
        List<EmailDto> emails = new LinkedList<>();
        emails.add(userClient.createEmail(returnedUserDto.getUserId(), emailDto));
        emails.add(userClient.createEmail(returnedUserDto.getUserId(), emailDto));

        EmailDto changed = userClient.updatePrimaryEmailByEmailId(emails.get(0).getUserId(), emails.get(0).getEmailId());
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
                .setEmailAddress("testemail@test.com")
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
