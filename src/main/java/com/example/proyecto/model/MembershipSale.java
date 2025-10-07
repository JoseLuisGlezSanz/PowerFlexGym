package com.example.proyecto.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "memberships_sales")
public class MembershipSale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_membership_sale")
    @JsonProperty("identificador de las ventas de membresía")
    private Integer idMembershipSales;
//
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

    //Llaves foraneas
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_customer")
    @JsonBackReference
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_membership")
    @JsonBackReference
    private Membership membership;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_gym")
    @JsonBackReference
    private Gym gym;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_user")
    @JsonBackReference
    private User user;
}