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

    public List<EmailDto> findAllEmails() {
        return emailRepository.findAll()
                .stream()
                .map(emailAssembler::assemble)
                .collect(Collectors.toList());
    }
    public List<EmailDto> findEmailsByUserId(UUID userId){
        return emailRepository.getAllByUserId(userId)
                .stream()
                .map(emailAssembler::assemble)
                .collect(Collectors.toList());
    }
    public EmailDto create(EmailDto dto) {
        Map<String, String> validationErrors = emailValidator.validate(dto);
        UUID emailId = dto.getEmailId() == null ? UUID.randomUUID() : dto.getEmailId();
        dto.setEmailId(emailId);

        if(!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors);
        }

        return Optional.of(dto)
                .map(emailAssembler::disassemble)
                .map(emailRepository::save)
                .map(emailAssembler::assemble)
                .get();
    }

}
