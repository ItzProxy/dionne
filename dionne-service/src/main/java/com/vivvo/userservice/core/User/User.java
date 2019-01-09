package com.vivvo.userservice.core.User;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
<<<<<<< HEAD:dionne-service/src/main/java/com/vivvo/userservice/core/User/User.java

import java.lang.reflect.Array;
import java.util.ArrayList;
=======
import java.util.List;
>>>>>>> master:dionne-service/src/main/java/com/vivvo/userservice/core/User.java
import java.util.UUID;

@Entity
@Table(name = "user")
@Getter
@Setter
@Accessors(chain = true)
public class User {

    @Id
    @Column(name = "user_id")
    @Type(type = "uuid-char")
    private UUID userId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "username")
    private String username;
}
