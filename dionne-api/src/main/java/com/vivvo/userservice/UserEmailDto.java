package com.vivvo.userservice;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
//FIXME delete this
public class UserEmailDto {
    //FIXME keep naming consistent
    private UUID userID;
    private UUID emailID;
    private String email;
    //FIXME weird name
    private Boolean bIsPrimary;
}
