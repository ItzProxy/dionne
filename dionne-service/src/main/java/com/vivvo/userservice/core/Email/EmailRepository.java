package com.vivvo.userservice.core.Email;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailRepository extends JpaRepository<Email, UUID> {

    List<Email> findByUserId(UUID userId);


    Boolean existsByEmail(String emailAddress);
    Email getEmailByUserIdAndIsPrimaryTrue(UUID userId);
}
