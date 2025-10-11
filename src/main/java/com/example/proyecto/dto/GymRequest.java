package com.example.proyecto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GymRequest {

    @NotBlank(message = "El nombre del gimnasio no puede estar vacío")
    @Size(max = 50, message = "El nombre del gimnasio no debe exceder los 50 caracteres")
    private String gym;
}
