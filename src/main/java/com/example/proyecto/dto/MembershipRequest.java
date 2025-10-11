package com.example.proyecto.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MembershipRequest {

    @NotBlank(message = "El nombre de la membresía no puede estar vacío")
    @Size(max = 50, message = "El nombre de la membresía no debe exceder los 50 caracteres")
    private String membership;

    @NotBlank(message = "La duración es obligatoria")
    @Size(max = 4, message = "La duración no debe exceder los 4 caracteres")
    private String duration;

    @NotNull(message = "El precio es obligatorio")
    @PositiveOrZero(message = "El precio no puede ser negativo")
    private BigDecimal price;

    @NotNull(message = "El estado es obligatorio")
    private Integer status;

    @NotNull(message = "El ID del gimnasio es obligatorio")
    private Integer idGym;
}
