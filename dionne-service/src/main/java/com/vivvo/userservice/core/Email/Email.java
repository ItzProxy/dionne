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
    @Type(type = "uuid-char")
    private UUID emailId;
    @Column(name = "user_id")
    @Type(type = "uuid-char")
    private UUID userId;
    @Column(name = "email_address")
    private String emailAddress;
    @Column(name = "is_primary_email")
    private Boolean isPrimary;

}
