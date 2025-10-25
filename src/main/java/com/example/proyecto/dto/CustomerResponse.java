package com.example.proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
@AllArgsConstructor
public class CustomerResponse {
    @JsonProperty("Customer identifier:")
    private Integer idCustomer;

    @JsonProperty("Customer name:")
    private String name;

    @JsonProperty("Neighborhood:")
    private String cologne;

    @JsonProperty("Phone number:")
    private String phone;

    @JsonProperty("Birth date:")
    private LocalDate birthDate;

    @JsonProperty("Medical condition:")
    private Boolean medicalCondition;

    @JsonProperty("Registration date:")
    private LocalDateTime registrationDate;

    @JsonProperty("Profile photo:")
    private String photo;

    @JsonProperty("ID photo:")
    private String photoCredential;

    @JsonProperty("Verified number:")
    private Boolean verifiedNumber;

    @JsonProperty("Gym information:")
    private GymResponse gym;

    @JsonProperty("Emergency contact:")
    private EmergencyContactResponse emergencyContact;
}