package com.vivvo.userservice.core;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user")
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
    @Column(name = "email")
    private String email;
    @Column(name = "is_primary")
    private Boolean bIsPrimary;
}
