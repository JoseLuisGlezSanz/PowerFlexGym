package com.example.proyecto.model;

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
@Table(name = "tickets_details")
public class TicketDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_datail_ticket")
    @JsonProperty("identificador del detalle")
    private Integer idDatailTicket;

    @Column(name = "amount")
    @JsonProperty("cantidad del producto")
    private Integer amount;

    @Column(name = "unit_price")
    @JsonProperty("precio unitario")
    private Double unitPrice;

    @Column(name = "subtotal")
    @JsonProperty("Subtotal del ticket")
    private Double subtotal;

    //Llaves foraneas
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_ticket")
    @JsonBackReference
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_product")
    @JsonBackReference
    private Product product;
}