package com.example.proyecto.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VisitRequest {

    private Integer idCustomer;

    @NotNull(message = "La fecha de la visita es obligatoria")
    private LocalDateTime date;

    @PositiveOrZero(message = "El valor pendiente no puede ser negativo")
    private Integer pending;

    @NotNull(message = "El ID del gimnasio es obligatorio")
    private Integer idGym;
}
