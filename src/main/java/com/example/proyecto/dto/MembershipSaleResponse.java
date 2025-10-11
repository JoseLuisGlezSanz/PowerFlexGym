package com.example.proyecto.dto;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Value
@Builder
public class MembershipSaleResponse {

    private Integer idMembershipSale;
    private LocalDateTime date;
    private Double payment;
    private Boolean cancellation;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer idMembership;
    private Integer idCustomer;
    private Integer idGym;
    private Integer idUser;
}
