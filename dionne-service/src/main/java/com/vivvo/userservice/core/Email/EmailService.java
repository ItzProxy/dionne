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
    private UserRepository userRepository;
    @Autowired
    private UserAssembler userAssembler;
    @Autowired
    private UserValidator userValidator;

    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private EmailAssembler emailAssembler;

    public List<UserDto> findAllUsers() {
        return userRepository.findAll()
            .stream()
            .map(userAssembler::assemble)
            .collect(Collectors.toList());
    }

    public UserDto create(UserDto dto) {
        Map<String, String> validationErrors = userValidator.validate(dto);
        if(!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors);
        }

        return Optional.of(dto)
                .map(userAssembler::disassemble)
                .map(userRepository::save)
                .map(userAssembler::assemble)
                .get();
    }

    public UserDto update(UserDto dto) {
        Map<String, String> validationErrors = userValidator.validate(dto);
        if(!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors);
        }

        User user = userAssembler.disassemble(dto);
        user = userRepository.save(user);
        return userAssembler.assemble(user);
    }

    public void delete(UUID userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        userRepository.delete(user);
    }

    public List<UserDto> findUserByLastName(String lastName){
        return userRepository.findByLastNameLike(lastName)
                .stream()
                .map(userAssembler::assemble)
                .collect(Collectors.toList());
    }

    public List<EmailDto> findEmailsByUserId(UUID userId){
        return emailRepository.getAllByUserId(userId)
                .stream()
                .map(emailAssembler::assemble)
                .collect(Collectors.toList());
    }
}
