package com.example.proyecto.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "customers_memberships")
public class CustomerMembership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_customer_membership")
    @JsonProperty("identificador de la membresia del cliente")
    private Integer idCustomerMembership;

    @ManyToOne
    @JoinColumn(name="id_customer")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name="id_membership")
    private Membership membership;

    @Column(name = "start_date")
    @JsonProperty("fecha de inicio de la membresia")
    private LocalDate startDate;

    @Column(name = "end_date")
    @JsonProperty("fecha de finalización de la membresia")
    private LocalDate endDate;

    @Column(name = "member_since", nullable = false)
    @JsonProperty("fecha de registro de la membresía")
    private LocalDateTime memberSince;

    @Column(name = "membership_status", nullable = false)
    @JsonProperty("estado de la membresía")
    private Boolean membershipStatus;

    // Relaciones
    @ManyToOne
    @JoinColumn(name = "id_gym")
    private Gym gym;
}