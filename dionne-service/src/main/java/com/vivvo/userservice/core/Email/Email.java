package com.vivvo.userservice.core.Email;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "email")
@Getter
@Setter
@Accessors(chain = true)
public class Email {
    @Id
    @Column(name = "email_id")
    private Integer emailId;
    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "email_address")
    private String email;
    @Column(name = "is_primary_email")
    private Boolean isPrimary;



}
