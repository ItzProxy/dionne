package com.vivvo.userservice.core.Email;

import com.vivvo.userservice.EmailDto;
import com.vivvo.userservice.UserDto;
import com.vivvo.userservice.core.User.*;
import com.vivvo.userservice.core.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    //no need for find all. always in context of user
    public List<EmailDto> findAllEmails() {
        return emailRepository.findAll()
                .stream()
                .map(emailAssembler::assemble)
                .collect(Collectors.toList());
    }

    public List<EmailDto> findEmailsByUserId(UUID userId){
        return emailRepository.findByUserId(userId)
                .stream()
                .map(emailAssembler::assemble)
                .collect(Collectors.toList());
    }
    //TODO Workflow, have at least one of the emails be primary at all times unless no email
    public EmailDto create(EmailDto dto) {
        //could also be part of disassemble
        UUID emailId = dto.getEmailId() == null ? UUID.randomUUID() : dto.getEmailId();
        dto.setEmailId(emailId);

        Map<String, String> validationErrors = emailValidator.validate(dto);
        if(!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors);
        }

        return Optional.of(dto)
                .map(emailAssembler::disassemble)
                .map(emailRepository::save)
                .map(emailAssembler::assemble)
                .get();
    }


    public EmailDto changeEmailPrimaryByEmailId(UUID userId, UUID emailId){
       // List<EmailDto> allEmailsByUserId = findEmailsByUserId(userId);
        List<Email> allEmailsByUserId = emailRepository.findByUserId(userId);
        if(allEmailsByUserId.size() == 0){
            throw new EmailNotFoundException(emailId);
        }
        if(allEmailsByUserId.stream().filter(e->e.getEmailId() == emailId).count() != 1) {
            throw new EmailNotFoundException(emailId);
        }
        Email toReturn = new Email();
        for(Email email : allEmailsByUserId){
            if(email.getEmailId() == emailId){
                email.setIsPrimary(true);
                toReturn = email;
            }
            else{
                email.setIsPrimary(false);
            }
            emailRepository.save(email);
        }
        return emailAssembler.assemble(toReturn);
    }



    public EmailDto makeEmailPrimaryByEmailId(UUID emailId){

        Email toChangeEmailPrimary = emailRepository.findById(emailId)
                .orElseThrow(() -> new EmailNotFoundException(emailId));

        List<Email> allEmailFromUser = emailRepository.findByUserId(toChangeEmailPrimary.getUserId());

        //.equals never ==
        allEmailFromUser.stream().forEach(e -> e.setIsPrimary(e.getEmailId().equals(emailId)));
        emailRepository.saveAll(allEmailFromUser);
        return emailAssembler.assemble(toChangeEmailPrimary);
    }
}
