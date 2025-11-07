package com.example.proyecto.model;

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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    @JsonProperty("identificador del usuario")
    private Long id;

    @Column(name = "user_name", nullable = false, length = 50)
    @JsonProperty("nombre de usuario")
    private String nameUser;

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

    //Relaciones
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<MembershipSale> membershipsSales;

    @ManyToOne
    @JoinColumn(name = "id_role")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "id_gym")
    private Gym gym;
}