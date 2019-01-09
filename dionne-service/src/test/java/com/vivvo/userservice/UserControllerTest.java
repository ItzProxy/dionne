package com.vivvo.userservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
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
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:teardown.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserControllerTest {

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
        UserDto userDto = newValidUser();

        testRestTemplate.postForLocation("/api/v1/users", userDto);

        UserDto[] users = testRestTemplate.getForObject("/api/v1/users", UserDto[].class);
        assertEquals(1, users.length);
        assertEquals("TestFirstName", users[0].getFirstName());
    }

    @Test
    public void testCreateAndSearchWithClient_shouldSucceed() {
        UserDto userDto = newValidUser();

        userClient.create(userDto);
        List<UserDto> users = userClient.findAddUsers();

        assertEquals(1, users.size());
        assertEquals("TestFirstName", users.get(0).getFirstName());
    }


    @Test(expected = NotFoundException.class)
    public void testDeleteInvalidId_shouldBeNotFound() {
        userClient.delete(UUID.randomUUID());
    }

    @Test
    public void testCreateUserAndUpdate_shouldSucceed() {
        UserDto userDto = newValidUser();

        UserDto createdDto = userClient.create(userDto);
        assertNotNull(createdDto.getUserId());

        createdDto
                .setFirstName("NewFirstName")
                .setLastName("NewLastName");

        UserDto updatedDto = userClient.update(createdDto);

        assertEquals("NewFirstName", updatedDto.getFirstName());
        assertEquals("NewLastName", updatedDto.getLastName());
    }

    @Test
    public void testCreateUserAndDeleteUser_shouldSucceed() {
        UserDto userDto = newValidUser();

        UserDto createdDto = userClient.create(userDto);
        assertNotNull(createdDto.getUserId());
        assertEquals(1, userClient.findAddUsers().size());

        userClient.delete(createdDto.getUserId());

        assertEquals(0, userClient.findAddUsers().size());
    }

    @Test
    public void testCreateWithInvalidFirstName_shouldReturnValidationErrors() {
        UserDto userDto = newValidUser();
        userDto.setFirstName(null);

        try {
            userClient.create(userDto);
            fail("Should have been BadRequestException.");
        } catch (BadRequestException e) {
            Map<String, String> errors = unwrapClientException(e);
            assertNotNull(errors.get("firstName"));
        }
    }

    private UserDto newValidUser() {
        return new UserDto()
                .setLastName("TestLastName")
                .setFirstName("TestFirstName")
                .setUsername("TestUsername")
                .setEmails(new ArrayList<String>(
                    Arrays.asList("dionne.pasion@gmail.com",
                            "dionne@vivvo.com"
                           )));
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
