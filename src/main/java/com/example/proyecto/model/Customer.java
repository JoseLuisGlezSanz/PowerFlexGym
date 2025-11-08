package com.example.proyecto.model;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_customer")
    @JsonProperty("identificador del cliente")
    private Long id;
    
    @Column(name = "name", nullable = false)
    @JsonProperty("nombre del cliente")
    private String name;

    @Column(name = "cologne", nullable = false)
    @JsonProperty("colonia del cliente")
    private String cologne;

    @Column(name = "phone", unique = true)
    @JsonProperty("telefono del cliente")
    private String phone;

    @Column(name = "birth_date", nullable = false)
    @JsonProperty("fecha de nacimiento del cliente")
    private Date birthDate;

    @Column(name = "medical_condition", nullable = false)
    @JsonProperty("condiciones medicas del cliente")
    private Boolean medicalCondition;

    @Column(name = "registration_date", nullable = false)
    @JsonProperty("fecha de registro del cliente")
    private LocalDateTime registrationDate;

    @Column(name = "photo", nullable = false)
    @JsonProperty("foto del cliente")
    private String photo;

    @Column(name = "photo_credential", nullable = false)
    @JsonProperty("foto de la credencial del cliente")
    private String photoCredential;

    @Column(name = "verified_number", nullable = false)
    @JsonProperty("numero verificado del cliente")
    private Boolean verifiedNumber;

    //Relaciones
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private EmergencyContact emergencyContact;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CustomerMembership> customerMemberships;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Visit> visits;

    @ManyToOne
    @JoinColumn(name = "gym_id")
    private Gym gym;
}