package com.example.proyecto.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "memberships")
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_membership")
    @JsonProperty("identificador de la membresía")
    private Integer idMembership;

    @Column(name = "membership", nullable = false)
    @JsonProperty("nombre de la membresía")
    private String membership;

    @Column(name = "duration", nullable = false, length = 4)
    @JsonProperty("duración de la membresía")
    private String duration;

    @Column(name = "price", nullable = false)
    @JsonProperty("precio de la membresía")
    private Double price;

    @Column(name = "status", nullable = false)
    @JsonProperty("estado de la membresía")
    private Integer status;

    //Relaciones
    @ManyToMany(mappedBy = "memberships")
    private List<Customer> customers;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_membership", referencedColumnName = "idMembership")
    private List<MembershipSale> membershipsSales;

    @ManyToOne
    @JoinColumn(name = "id_gym")
    private Gym gym;
}