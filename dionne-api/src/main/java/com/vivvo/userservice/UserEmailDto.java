package com.vivvo.userservice;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class UserEmailDto {
    private UUID userID;
    private UUID emailID;
    private String email;
    private Boolean bIsPrimary;
}
