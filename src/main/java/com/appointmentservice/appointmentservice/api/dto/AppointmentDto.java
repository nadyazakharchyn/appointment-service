package com.appointmentservice.appointmentservice.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppointmentDto {
    private Long userId;
    private Long doctorId;
    private String date;
}
