package com.vivvo.userservice.core.SubResources;

import com.vivvo.userservice.EmailDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Array;
import java.util.List;
import java.util.UUID;

@Repository
public interface EmailRepository extends JpaRepository<Email, UUID> {

    List<Email> getAllByUserId(UUID emailId);

}
