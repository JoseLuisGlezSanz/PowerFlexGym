package com.example.proyecto.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CustomerRequest {
    private String name;
    private String cologne;
    @Pattern(regexp = "\\d{10}") private String phone;
    private LocalDate birthDate;
    private Boolean medicalCondition;
    private Integer idGym;
    private String photo;
    private String photoCredential;
    private Boolean verifiedNumber = false;
}