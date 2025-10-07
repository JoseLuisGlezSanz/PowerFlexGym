package com.example.proyecto.model;

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
@Table(name = "visits")
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_visit")
    @JsonProperty("identificador de la visita")
    private Integer idVisit;

    @Column(name ="id_customer")
    @JsonProperty("identificador del cliente")
    private Integer idCustomer;

    @Column(name = "date", nullable = false)
    @JsonProperty("fecha de la visita")
    private LocalDateTime date;
    
    @Column(name = "pending")
    @JsonProperty("visita pendiente")
    private Integer pending;

    //Llaves foraneas
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_gym")
    @JsonBackReference
    private Gym gym;
}
