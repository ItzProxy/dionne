package com.vivvo.userservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import javax.ws.rs.ClientErrorException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
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

    @Before
    public void before() {
        emailClient = new EmailClient();
        emailClient.setBaseUri("http://localhost:" + port);
    }

    @Test
    public void testCreateAndSearch_shouldSucceed() {
        EmailDto emailDto = validEmail();

        testRestTemplate.postForLocation("/api/v1/users/email" , emailDto);

        EmailDto[] emails = testRestTemplate.getForObject("/api/v1/users/email", EmailDto[].class);
        assertEquals(1, emails.length);
        assertEquals("testemail@test.com", emails[0].getEmail());
    }

    @Test
    public void testCreateAndSearchWithClient_shouldSucceed() {
        EmailDto emailDto = validEmail();

        emailClient.create(emailDto);
        List<EmailDto> emails = emailClient.findAllEmails();

        assertEquals(1, emails.size());
        assertEquals("testemail@test.com", emails.get(0).getEmail());
    }

    private EmailDto validEmail() {
        return new EmailDto()
                .setUserId(UUID.randomUUID())
                .setEmail("testemail@test.com")
                .setIsPrimary(false);
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
