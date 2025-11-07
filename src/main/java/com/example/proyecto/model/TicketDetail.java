package com.example.proyecto.model;

import java.math.BigDecimal;

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
    @Column(name = "id_detail_ticket")
    @JsonProperty("identificador del detalle")
    private Long id;

    @Column(name = "amount")
    @JsonProperty("cantidad del producto")
    private Integer amount;

    @Column(name = "unit_price")
    @JsonProperty("precio unitario")
    private BigDecimal unitPrice;

    @Column(name = "subtotal")
    @JsonProperty("Subtotal del ticket")
    private BigDecimal subtotal;

    // Relaciones
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;
}