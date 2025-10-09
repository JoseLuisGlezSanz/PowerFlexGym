package com.example.proyecto.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    @JsonProperty("identificador del usuario")
    private Integer idUser;

    @Column(name = "user", nullable = false, length = 50)
    @JsonProperty("nombre de usuario")
    private String user;

    @Column(name = "mail", nullable = false, length = 255)
    @JsonProperty("correo del usuario")
    private String mail;

    @Column(name = "phone", nullable = false, length = 10)
    @JsonProperty("telefono del usuario")
    private String phone;

    @Column(name = "name", nullable = false, length = 150)
    @JsonProperty("nombre completo del usuario")
    private String name;

    @Column(name = "password", nullable = false, length = 255)
    @JsonProperty("contraseña del usuario")
    private String password;

    @Column(name = "status", nullable = false)
    @JsonProperty("estado del usuario")
    private Integer status;

    //Laves foraneas
    @ManyToOne
    @JoinColumn(name = "id_role", referencedColumnName = "id_role", nullable = false)
    @JsonBackReference
    @JsonProperty("rol del usuario")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "id_gym", referencedColumnName = "id_gym", nullable = false)
    @JsonBackReference
    @JsonProperty("gimnasio del usuario")
    private Gym gym;

    //Relaciones
    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MembershipSale> membershipSales;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;
}