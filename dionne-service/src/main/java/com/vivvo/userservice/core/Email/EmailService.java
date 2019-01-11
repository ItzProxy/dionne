package com.vivvo.userservice.core.Email;

import com.fasterxml.jackson.databind.JsonNode;
import com.vivvo.userservice.EmailDto;
import com.vivvo.userservice.core.MailgunConfigurations;
import com.vivvo.userservice.core.ValidationException;
import jdk.nashorn.internal.runtime.options.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmailService {
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private EmailAssembler emailAssembler;
    @Autowired
    private EmailValidator emailValidator;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MailgunConfigurations mailgunConfigurations;

    public EmailDto findOneEmailByEmailId(UUID userId, UUID emailId){
        return Optional.of(emailRepository.findEmailByUserIdAndEmailId(userId, emailId))
            .map(emailAssembler::assemble).get();
    }

    public List<EmailDto> findAllEmails(UUID userId) {
        return emailRepository.findAllByUserId(userId)
                .stream()
                .map(emailAssembler::assemble)
                .collect(Collectors.toList());
    }
    //TODO Workflow, have at least one of the emails be primary at all times unless no emailAddress
    public EmailDto create(UUID userId, EmailDto dto) {
        Map<String, String> validationErrors = emailValidator.validate(userId, dto);

        if(!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors);
        }

        UUID emailId = dto.getEmailId() == null ? UUID.randomUUID() : dto.getEmailId();
        dto.setEmailId(emailId);

        Boolean isPrimary = dto.getIsPrimary() == null ? false : dto.getIsPrimary();
        dto.setIsPrimary(isPrimary);

        Boolean isVerified = dto.getIsVerified() == null ? false : dto.getIsVerified();
        dto.setIsPrimary(isVerified);

        return Optional.of(dto)
                .map(emailAssembler::disassemble)
                .map(emailRepository::save)
                .map(emailAssembler::assemble)
                .get();
    }

    public void delete(UUID userId, UUID emailId){
        Email toDelete = Optional.of(emailRepository.getEmailByEmailId(emailId))
            .orElseThrow(() -> new EmailNotFoundException(emailId));

        if(toDelete.getUserId() != userId){
            throw new EmailUserIdNoMatchException(userId, emailId);
        }

        emailRepository.delete(toDelete);
    }

    public EmailDto makeEmailPrimaryByEmailId(UUID userId,UUID emailId){
        Email toChangeEmailPrimary = Optional.of(emailRepository.getEmailByEmailId(emailId))
                .orElseThrow(() -> new EmailNotFoundException(emailId));

        List<Email> allEmailFromUser = emailRepository.findAllByUserId(userId);

        allEmailFromUser
                .stream()
                .forEach(e -> e.setIsPrimary(e.getEmailId().equals(emailId)));
        emailRepository.saveAll(allEmailFromUser);
        return emailAssembler.assemble(toChangeEmailPrimary);
    }

    public Boolean sendEmail(UUID userId){
        //check if user has a primary set if not send false
        Email primaryEmailToSendTo = Optional.of(emailRepository
            .findEmailByUserIdAndIsPrimaryIsTrue(userId).orElseThrow(() -> new UserHasNoPrimaryEmail(userId));

        //if so then set up mailgun client and throw em an email
        restTemplate.
        HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + YOUR_DOMAIN_NAME + "/messages")
            .basicAuth("api", mailgunConfigurations.get)
            .field("from", "Excited User <USER@YOURDOMAIN.COM>")
            .field("to", "artemis@example.com")
            .field("subject", "hello")
            .field("text", "testing")
            .asJson();

        return request.getBody();
    }

}
