package com.example.proyecto.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "memberships_sales")
public class MembershipSale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_membership_sale")
    @JsonProperty("identificador de las ventas de membresía")
    private Integer idMembershipSales;

    @Column(name = "date", nullable = false)
    @JsonProperty("fecha de la venta")
    private LocalDateTime date;

    @Column(name = "payment", nullable = false)
    @JsonProperty("pago de la membresia")
    private Double payment;

    @Column(name = "cancellation", nullable = false)
    @JsonProperty("cancelacion de la membresia")
    private Boolean cancellation;
    
    @Column(name = "start_date", nullable = false)
    @JsonProperty("fecha de inicio de la membresia")
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    @JsonProperty("fecha de finalización de la membresia")
    private LocalDate endDate;
}