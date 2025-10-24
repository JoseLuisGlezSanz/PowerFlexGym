package com.example.proyecto.dto;

import lombok.Data;
import java.time.LocalDate;

import jakarta.validation.constraints.Pattern;

@Data
public class CustomerRequest {
    private String name;
    private String cologne;
    @Pattern(regexp = "\\d{10}") private String phone;
    private LocalDate birthDate;
    private Boolean medicalCondition;
    private String photo;
    private String photoCredential;
    private Boolean verifiedNumber;
    private Integer idGym;
    private String contactName;
    private String contactPhone;
}