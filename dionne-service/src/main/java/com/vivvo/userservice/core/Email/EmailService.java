package com.vivvo.userservice.core.Email;

import com.sun.jndi.toolkit.url.Uri;
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

import javax.transaction.Transactional;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.*;
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

    @Autowired
    private MailgunConfigurations mailgunConfigurations;

    Map<UUID,UUID> unverifiedEmails = new HashMap<>();

    public EmailDto findOneEmailByEmailId(UUID userId, UUID emailId){
        Email toReturn = Optional.of(emailRepository.findEmailByUserIdAndEmailId(userId, emailId))
            .orElseThrow(()->new EmailNotFoundException(emailId));

        return emailAssembler.assemble(toReturn);
    }

    public List<EmailDto> findAllEmails(UUID userId) {
        return emailRepository.findAllByUserId(userId)
                .stream()
                .map(emailAssembler::assemble)
                .collect(Collectors.toList());
    }
    public EmailDto create(UUID userId, EmailDto dto) {
        Map<String, String> validationErrors = emailValidator.validate(userId, dto);

        if(!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors);
        }

        UUID emailId = dto.getEmailId() == null ? UUID.randomUUID() : dto.getEmailId();
        dto.setEmailId(emailId);

        Boolean isPrimary = dto.getIsPrimary() == null ? false : dto.getIsPrimary();
        dto.setIsPrimary(isPrimary);
        dto.setIsVerified(false); //no man should be verified

        EmailDto toReturn = Optional.of(dto)
                .map(emailAssembler::disassemble)
                .map(emailRepository::save)
                .map(emailAssembler::assemble)
                .get();

        if(isPrimary){
            makeEmailPrimaryByEmailId(userId,emailId);
        }

        return toReturn;
    }

    public void delete(UUID userId, UUID emailId){
        Email toDelete = emailRepository.findById(emailId)
            .orElseThrow(() -> new EmailNotFoundException(emailId));

        if(!toDelete.getUserId().equals(userId)){
            throw new EmailUserIdNoMatchException(userId, emailId);
        }

        emailRepository.delete(toDelete);
    }

    public EmailDto makeEmailPrimaryByEmailId(UUID userId,UUID emailId){
        Email toChangeEmailPrimary = emailRepository.findById(emailId)
            .orElseThrow(() -> new EmailNotFoundException(emailId));

        List<Email> allEmailFromUser = emailRepository.findAllByUserId(userId);

        allEmailFromUser
                .stream()
                .forEach(e -> e.setIsPrimary(e.getEmailId().equals(emailId)));
        emailRepository.saveAll(allEmailFromUser);
        return emailAssembler.assemble(toChangeEmailPrimary);
    }

    public Response sendEmailToPrimary(UUID userId, String subject, String content){
        //check if user has a primary set if not send false
        Email primaryEmailToSendTo = Optional.of(emailRepository.findEmailByUserIdAndIsPrimaryIsTrue(userId))
            .orElseThrow(()->new UserHasNoPrimaryEmail(userId));

        return sendEmailToEmailAddress(primaryEmailToSendTo.getEmailAddress(),subject,content);
    }

    public Boolean emailVerificationCheck(UUID userId,UUID emailId, UUID verificationId){
        if(unverifiedEmails.containsKey(emailId)){
           if(unverifiedEmails.get(emailId).equals(verificationId)){
               //we can now update the database with this shit
               setEmailIsVerifiedToTrue(userId,emailId);
               unverifiedEmails.remove(emailId);
               return true;
           }
        }
        return false;
    }

    public Boolean sendVerificationEmail(UUID userId, UUID emailId, UUID verificationId, URI exposeEndPoint){

        String emailAddressToVerify = emailRepository.findEmailByUserIdAndEmailId(userId,emailId).getEmailAddress();

        String subject = "Verify Email";
        StringBuilder content = new StringBuilder("Please verify your email - " + emailAddressToVerify);

        UriBuilder verificationEmailLink = UriBuilder
            .fromUri(exposeEndPoint)
            .path(userId.toString())
            .path("emails")
            .path(emailId.toString())
            .path("verify")
            .path(verificationId.toString());

        content.append("\n Click the link to verify " + verificationEmailLink.toString());

        addNewEmailToBeVerified(emailId,verificationId);

        return sendEmailToEmailAddress(emailAddressToVerify,subject,content.toString()).isOk();
    }

    private void setEmailIsVerifiedToTrue(UUID userId,UUID emailId){
        Email email = Optional.of(emailRepository.findEmailByUserIdAndEmailId(userId,emailId))
            .orElseThrow(() -> new EmailNotFoundException(emailId));

        email.setIsVerified(true);
        emailRepository.save(email);
    }

    private Response sendEmailToEmailAddress(String emailAddressToSendTo, String subject, String content){
        Configuration configuration = setupConfiguration();
        return Mail.using(configuration)
            .to(emailAddressToSendTo)
            .subject(subject)
            .text(content)
            .build()
            .send();
    }

    private Configuration setupConfiguration() {
        return new Configuration()
            .domain(mailgunConfigurations.getDomainname())
            .apiKey(mailgunConfigurations.getKey())
            .from("Test account", "mailguntest@" + mailgunConfigurations.getDomainname());
    }

    private void addNewEmailToBeVerified(UUID emailId, UUID verificationId){
        unverifiedEmails.put(emailId,verificationId);
    }

}
