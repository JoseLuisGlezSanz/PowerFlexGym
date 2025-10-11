package com.example.proyecto.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ticket")
    @JsonProperty("identificador del ticket")
    private Integer idTicket;
    
    @Column(name = "date", nullable = false)
    @JsonProperty("fecha de creación")
    private LocalDateTime date;

    @Column(name = "id_customer")
    @JsonProperty("identificador del cliente")
    private Integer idCustomer;

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

    //Laves foraneas
    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id_user", nullable = false)
    @JsonBackReference
    @JsonProperty("ticket del usuario")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_customer", referencedColumnName = "id_customer", nullable = false)
    @JsonBackReference
    @JsonProperty("ticket del cliente")
    private Customer customer;

    //Relaciones
    @JsonManagedReference
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketDetail> ticketDetails;
}