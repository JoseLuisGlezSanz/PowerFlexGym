package com.example.proyecto.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
@Table(name = "visits")
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_visit")
    @JsonProperty("identificador de la visita")
    private Integer idVisit;

    @Column(name = "date", nullable = false)
    @JsonProperty("fecha de la visita")
    private LocalDateTime date;
    
    @Column(name = "pending")
    @JsonProperty("visita pendiente")
    private Integer pending;

    //Llaves foraneas
    @ManyToOne  
    @JoinColumn(name="id_gym", referencedColumnName = "id_gym", nullable = false)
    @JsonBackReference
    @JsonProperty("visita al gym")
    private Gym gym;
}