package com.example.proyecto.model;

import java.sql.Date;
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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customers_memberships")
@IdClass(CustomerMembershipPK.class)
public class CustomerMembership {
    @Id
    @ManyToOne
    @JoinColumn(name="id_customer")
    @JsonProperty("identificador del cliente")
    private Integer idCustomer;

    @Id
    @ManyToOne
    @JoinColumn(name="id_membership")
    @JsonProperty("identificador de la membresia")
    private Integer idMembership;

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
    @ManyToOne
    @JoinColumn(name="id_gym", referencedColumnName = "id_gym", nullable = false)
    @JsonBackReference
    @JsonProperty("membresia del cliente en el gym")
    private Gym gym;
}