package com.example.proyecto.model;

import java.sql.Date;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customers_memberships")
public class CustomerMembership {
    @Column(name = "start_date")
    @JsonProperty("fecha de inicio de la membresia")
    private Date startDate;

    @Column(name = "end_date")
    @JsonProperty("fecha de finalización de la membresia")
    private Date endDate;

    @Column(name = "member_since", nullable = false)
    @JsonProperty("fecha de registro de la membresía")
    private LocalDateTime memberSince;

    @Column(name = "membership_status", nullable = false)
    @JsonProperty("estado de la membresía")
    private Boolean membershipStatus;

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
}
