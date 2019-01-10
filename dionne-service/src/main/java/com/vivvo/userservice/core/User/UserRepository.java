package com.vivvo.userservice.core.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    List<User> findByLastNameLike(String lastName);

    Boolean existsByUsername(String username);

    Optional<User> findByUserId(UUID userId);

}
