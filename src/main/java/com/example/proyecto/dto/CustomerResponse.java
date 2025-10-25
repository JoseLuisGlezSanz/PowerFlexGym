package com.example.proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {
    @JsonProperty("Customer identifier:")
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