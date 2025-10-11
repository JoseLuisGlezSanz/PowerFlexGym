package com.example.proyecto.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
@IdClass(CustomerMembershipId.class)
public class CustomerMembership {
    @Id
    @ManyToOne
    @JoinColumn(name="id_customer")
    @JsonBackReference
    private Customer customer;

    @Id
    @ManyToOne
    @JoinColumn(name="id_membership")
    @JsonBackReference
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

    //Llaves foraneas
    @ManyToOne
    @JoinColumn(name="id_gym", referencedColumnName = "id_gym", nullable = false)
    @JsonBackReference
    @JsonProperty("membresia del cliente en el gym")
    private Gym gym;
}