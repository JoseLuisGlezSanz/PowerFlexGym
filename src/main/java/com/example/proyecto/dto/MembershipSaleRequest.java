package com.example.proyecto.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MembershipSaleRequest {

    @NotNull(message = "La fecha de venta es obligatoria")
    private LocalDateTime date;

    @NotNull(message = "El pago es obligatorio")
    @PositiveOrZero(message = "El pago no puede ser negativo")
    private Double payment;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate startDate;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate endDate;

    @NotNull(message = "El ID de la membresía es obligatorio")
    private Integer idMembership;

    private Integer idCustomer;

    @NotNull(message = "El ID del gimnasio es obligatorio")
    private Integer idGym;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Integer idUser;

    private Boolean cancellation = false;
}
