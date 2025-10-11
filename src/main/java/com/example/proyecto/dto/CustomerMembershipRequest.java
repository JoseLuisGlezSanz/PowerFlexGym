package com.example.proyecto.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerMembershipRequest {

    @NotNull(message = "El ID del cliente es obligatorio")
    private Integer idCustomer;

    @NotNull(message = "El ID de la membresía es obligatorio")
    private Integer idMembership;

    @NotNull(message = "El ID del gimnasio es obligatorio")
    private Integer idGym;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate startDate;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate endDate;

    @NotNull(message = "El estado de la membresía es obligatorio")
    private Boolean membershipStatus;
}
