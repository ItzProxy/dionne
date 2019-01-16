package com.vivvo.userservice.core.Phone;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "user")
@Getter
@Setter
@Accessors(chain = true)
public class Phone {

    @Id
    @Column(name = "phone_id")
    @Type(type = "uuid-char")
    private UUID phoneId;
    @Column(name = "user_id")
    @Type(type = "uuid-char")
    private UUID userId;
    @Column(name = "phone_numnber")
    private String phoneNumber;
    @Column(name = "is_primary_phone")
    private Boolean isPrimary;
    @Column(name = "is_verified_phone")
    private Boolean isVerified;

}
