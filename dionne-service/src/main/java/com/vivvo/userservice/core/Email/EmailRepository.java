package com.vivvo.userservice.core.Email;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmailRepository extends JpaRepository<Email, UUID> {

    List<Email> getAllByUserId(UUID userId);
    Email getEmailByEmailId(UUID emailId);
    Boolean existsByEmail(String emailAddress);
}
