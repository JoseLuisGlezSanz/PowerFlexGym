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
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
@Table(name = "emergencys_contacts")
public class EmergencyContact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contact")
    @JsonProperty("identificador del contacto de emergencia")
    private Integer idContact;

    @Column(name = "contact_name", nullable = false)
    @JsonProperty("nombre del contacto de emergencia")
    private String contactName;

    @Column(name = "contact_phone", nullable = false)
    @JsonProperty("telefono del contacto de emergencia")
    private String contactPhone;

    //Llaves foraneas
    @OneToOne
    @JoinColumn(name = "id_customer", referencedColumnName = "id_customer", nullable = false, unique = true)
    @JsonProperty("cliente asociado")
    private Customer customer;
}