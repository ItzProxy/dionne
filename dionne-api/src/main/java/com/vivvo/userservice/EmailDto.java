package com.vivvo.userservice;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class EmailDto {
    private UUID userId;
    private Integer emailId;
    private String email;
    private Boolean isPrimary;
}
