package com.vivvo.userservice.core.Email;

import com.vivvo.userservice.EmailDto;
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

        return Optional.of(dto)
                .map(emailAssembler::disassemble)
                .map(emailRepository::save)
                .map(emailAssembler::assemble)
                .get();
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
}
