package com.example.proyecto.model;

import java.math.BigDecimal;
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
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ticket")
    @JsonProperty("identificador del ticket")
    private Long id;
    
    @Column(name = "date", nullable = false)
    @JsonProperty("fecha de creación")
    private LocalDateTime date;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    @JsonProperty("total del ticket")
    private BigDecimal total;

    @Column(name = "status", nullable = false)
    @JsonProperty("estado del ticket")
    private Integer status;

    @Column(name = "sale_date")
    @JsonProperty("fecha de venta")
    private LocalDateTime saleDate;

    @Column(name = "method_payment")
    @JsonProperty("forma de pago")
    private Integer methodPayment;

    @Column(name = "payment_with", precision = 10, scale = 2)
    @JsonProperty("monto con el que se pagó")
    private BigDecimal paymentWith;

    //Relaciones
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<TicketDetail> ticketsDetails;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}