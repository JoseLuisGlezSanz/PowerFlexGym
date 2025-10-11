package com.example.proyecto.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MembershipSaleRequest {
    private LocalDateTime date;
    private Double payment;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer idMembership;
    private Integer idCustomer;
    private Integer idGym;
    private Integer idUser;
    private Boolean cancellation = false;
}
