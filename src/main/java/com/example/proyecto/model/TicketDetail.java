package com.example.proyecto.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    // Relaciones
    @ManyToOne
    @JoinColumn(name = "id_product")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "id_ticket")
    private Ticket ticket;
}