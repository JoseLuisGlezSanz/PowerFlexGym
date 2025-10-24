package com.example.proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CustomerResponse {
    private Integer idCustomer;
    private String name;
    private String cologne;
    private String phone;
    private LocalDate birthDate;
    private Boolean medicalCondition;
    private LocalDateTime registrationDate;
    private String photo;
    private String photoCredential;
    private Boolean verifiedNumber;
    private GymResponse gym;
    private EmergencyContactResponse emergencyContact;
}