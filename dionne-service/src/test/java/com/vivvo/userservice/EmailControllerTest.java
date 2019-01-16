package com.vivvo.userservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vivvo.userservice.core.Email.Email;
import com.vivvo.userservice.core.Email.Exceptions.EmailNotFoundException;
import com.vivvo.userservice.core.Email.Exceptions.EmailUserIdNoMatchException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StreamUtils;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.NotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;

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
        assertEquals("test@test.com", emails[0].getEmailAddress());
    }

    @Test
    public void testCreateAndSearchWithClient_shouldSucceed() {
        UserDto userDto = validUser();
        UserDto returnedUserDto = userClient.create(userDto);


        EmailDto emailDto = validEmail(returnedUserDto.getUserId());
        userClient.createEmail(emailDto.getUserId(), emailDto);

        List<EmailDto> emails = userClient.findEmailsByUserId(returnedUserDto.getUserId());

        assertEquals(1, emails.size());
        assertEquals("test@test.com", emails.get(0).getEmailAddress());
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
        assertEquals("test@test.com", emails.get(0).getEmailAddress());
    }
    @Test
    public void testCreateAndSetPrimaryForOneTrue_shouldSucceed(){
        UserDto userDto = validUser();
        UserDto returnedUserDto = userClient.create(userDto);


        EmailDto emailDto = validEmail(returnedUserDto.getUserId());
        List<EmailDto> emails = new LinkedList<>();
        emails.add(userClient.createEmail(returnedUserDto.getUserId(), emailDto));
        emails.add(userClient.createEmail(returnedUserDto.getUserId(), emailDto));

        userClient.updatePrimaryEmailByEmailId(returnedUserDto.getUserId(),
            emails.get(0).getEmailId());

        EmailDto changed = userClient.findEmailByEmailId(returnedUserDto.getUserId(),emails.get(0).getEmailId());
        EmailDto getSecondEmailToCheckForPrimary = userClient.findEmailByEmailId(returnedUserDto.getUserId(),
            emails.get(1).getEmailId());
        assertEquals(true, changed.getIsPrimary());
        assertEquals(false, getSecondEmailToCheckForPrimary.getIsPrimary());
    }

    @Test(expected = EmailNotFoundException.class)
    public void testCreateAndSetPrimaryForOneTrue_shouldError(){
        UserDto userDto = validUser();
        UserDto returnedUserDto = userClient.create(userDto);


        EmailDto emailDto = validEmail(returnedUserDto.getUserId());
        List<EmailDto> emails = new LinkedList<>();
        emails.add(userClient.createEmail(returnedUserDto.getUserId(), emailDto));
        emails.add(userClient.createEmail(returnedUserDto.getUserId(), emailDto));

        userClient.updatePrimaryEmailByEmailId(returnedUserDto.getUserId(),
            emails.get(0).getEmailId());

        userClient.findEmailByEmailId(returnedUserDto.getUserId(),UUID.randomUUID());
    }

    @Test(expected = NotFoundException.class)
    public void testMakePrimaryWithInvalidEmailId_shouldErrorNotFoundException(){
        UserDto userDto = validUser();
        UserDto returnedUserDto = userClient.create(userDto);
        userClient.updatePrimaryEmailByEmailId(returnedUserDto.getUserId(),UUID.randomUUID());
    }

    @Test
    public void testDeleteWithValidEmailId_shouldSucceed(){
        UserDto userDto = validUser();
        UserDto returnedUserDto = userClient.create(userDto);
        EmailDto emailDto = validEmail(returnedUserDto.getUserId());

        assertNotNull(emailDto);
        EmailDto returnEmail = userClient.createEmail(returnedUserDto.getUserId(),emailDto);
        List<EmailDto> emails = userClient.findEmailsByUserId(returnedUserDto.getUserId());
        assertEquals(1,emails.size());
        assertEquals("test@test.com", emails.get(0).getEmailAddress());

        userClient.deleteEmail(returnedUserDto.getUserId(),returnEmail.getEmailId());
        List<EmailDto> shouldBeNoEmails = userClient.findEmailsByUserId(returnedUserDto.getUserId());
        assertEquals(0,shouldBeNoEmails.size());
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteWithInvalidEmailId_shouldErrorEmailNotFoundException(){
        UserDto userDto = validUser();
        UserDto returnedUserDto = userClient.create(userDto);
        EmailDto emailDto = validEmail(returnedUserDto.getUserId());
        assertNotNull(emailDto);
        userClient.deleteEmail(returnedUserDto.getUserId(),UUID.randomUUID());
    }

    @Test(expected = EmailUserIdNoMatchException.class)
    public void testDeleteWithInvalidUserId_shouldErrorEmailNotFoundException(){
        UserDto userDto = validUser();
        UserDto returnedUserDto = userClient.create(userDto);
        EmailDto emailDto = validEmail(returnedUserDto.getUserId());

        assertNotNull(emailDto);
        userClient.createEmail(returnedUserDto.getUserId(), emailDto);

        assertEquals(1,userClient.findEmailsByUserId(returnedUserDto.getUserId()).size());

        userClient.deleteEmail(UUID.randomUUID(),emailDto.getEmailId());
    }

    @Test
    public void testSendEmailToPrimary_shouldFail(){
        UserDto userDto = validUser();
        UserDto returnedUserDto = userClient.create(userDto);


        EmailDto emailDto = validEmail(returnedUserDto.getUserId());
        List<EmailDto> emails = new LinkedList<>();
        emails.add(userClient.createEmail(returnedUserDto.getUserId(), emailDto));
        emails.add(userClient.createEmail(returnedUserDto.getUserId(), emailDto));

        userClient.updatePrimaryEmailByEmailId(returnedUserDto.getUserId(),
            emails.get(0).getEmailId());

        assertEquals(false, userClient.sendEmailToPrimary(returnedUserDto.getUserId()));
    }



    private EmailDto validEmail(UUID userId) {
        return new EmailDto()
                .setUserId(userId == null ? UUID.randomUUID() : userId)
                .setEmailAddress("test@test.com")
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
