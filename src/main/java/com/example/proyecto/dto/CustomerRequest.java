package com.example.proyecto.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CustomerRequest {
    private String name;
    private String cologne;
    private String phone;
    private LocalDate birthDate;
    private Boolean medicalCondition;
    private LocalDateTime registrationDate;
    private String photo;
    private String photoCredential;
    private Boolean verifiedNumber = false;
}