package com.example.proyecto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmergencyContactRequest {

    @NotBlank(message = "El nombre del contacto no puede estar vacío")
    private String contactName;

    @NotBlank(message = "El teléfono del contacto no puede estar vacío")
    @Pattern(regexp = "\\d{10}", message = "El teléfono del contacto debe tener exactamente 10 dígitos")
    private String contactPhone;

    @NotNull(message = "El ID del cliente es obligatorio")
    private Integer idCustomer;
}
