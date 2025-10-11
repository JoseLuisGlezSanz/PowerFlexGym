package com.example.proyecto.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CustomerRequest {
    @NotBlank private String name;
    @NotBlank private String cologne;
    @Pattern(regexp = "\\d{10}") private String phone;
    @NotNull private LocalDate birthDate;
    @NotNull private Boolean medicalCondition;
    @NotNull private Integer idGym;
}