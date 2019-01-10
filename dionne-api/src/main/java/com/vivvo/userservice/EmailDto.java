package com.vivvo.userservice;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class EmailDto {
    private UUID userId;
    private UUID emailId;
    private String emailAddress;
    private Boolean isPrimary;
    private Boolean isVerified;
}
