package com.example.proyecto.model;

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
@Table(name = "memberships")
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_membership")
    @JsonProperty("identificador de la membresía")
    private Long id;

    @Column(name = "membership", nullable = false)
    @JsonProperty("nombre de la membresía")
    private String name;

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
    @ManyToOne
    @JoinColumn(name = "gym_id")
    private Gym gym;
}