package com.appointmentservice.appointmentservice.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

    private String firstName;
    private String lastName;
    private String email;
}
