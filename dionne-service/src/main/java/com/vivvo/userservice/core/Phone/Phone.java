package com.vivvo.userservice.core.Phone;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Getter
@Setter
@Accessors(chain = true)
public class Phone {

    @Id @GeneratedValue
    @Column(name = "phone_id")
    @Type(type = "uuid-char")
    private int phoneId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "username")
    private String username;
}
