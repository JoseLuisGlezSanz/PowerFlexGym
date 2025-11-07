package com.example.proyecto.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "emergencys_contacts")
public class EmergencyContact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contact")
    @JsonProperty("identificador del contacto de emergencia")
    private Long id;

    @Column(name = "contact_name", nullable = false)
    @JsonProperty("nombre del contacto de emergencia")
    private String contactName;

    @Column(name = "contact_phone", nullable = false)
    @JsonProperty("telefono del contacto de emergencia")
    private String contactPhone;

    //Relaciones
    @OneToOne
    @JoinColumn(name = "customer_id", nullable = false, unique = true)
    private Customer customer;
}