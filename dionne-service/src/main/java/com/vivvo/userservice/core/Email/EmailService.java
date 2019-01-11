package com.vivvo.userservice.core.Email;

import com.vivvo.userservice.EmailDto;
import com.vivvo.userservice.core.Email.Exceptions.EmailNotFoundException;
import com.vivvo.userservice.core.Email.Exceptions.EmailUserIdNoMatchException;
import com.vivvo.userservice.core.Email.Exceptions.UserHasNoPrimaryEmail;
import com.vivvo.userservice.core.MailgunConfigurations;
import com.vivvo.userservice.core.ValidationException;
import lombok.extern.slf4j.Slf4j;
import net.sargue.mailgun.Configuration;
import net.sargue.mailgun.Mail;
import net.sargue.mailgun.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
@Slf4j
@Service
@Transactional
public class EmailService {
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private EmailAssembler emailAssembler;
    @Autowired
    private EmailValidator emailValidator;

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

    public Response sendEmail(UUID userId, String subject, String content){
        //check if user has a primary set if not send false
        Email primaryEmailToSendTo = Optional.of(emailRepository.findEmailByUserIdAndIsPrimaryIsTrue(userId))
            .orElseThrow(()->new UserHasNoPrimaryEmail(userId));

        //if so then set up mailgun client and throw em an email
        // Set headers for the request
        Configuration configuration = new Configuration()
            .domain("sandboxbd2749017ccc486bb974b8117ca206e7.mailgun.org")//mailgunConfigurations.getDomainname())
            .apiKey("552be8de429e7c1293002c55002c029f-060550c6-6cd81e14")//mailgunConfigurations.getKey())
            .from("Test account", "mailguntest@sandboxbd2749017ccc486bb974b8117ca206e7.mailgun.org"); //+ mailgunConfigurations.getDomainname());
        log.warn("key: " + configuration.apiKey() +
            "\ndomain: "+configuration.domain() +
            "\nfrom: " +configuration.from());
        Response a = Mail.using(configuration)
            .to("dionne.pasion@gmail.com")//primaryEmailToSendTo.getEmailAddress())
            .subject(subject)
            .text(content)
            .build()
            .send();
        log.warn(new Integer(a.responseCode()).toString());
        return a;
    }
}
