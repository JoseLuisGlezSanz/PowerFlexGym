package com.example.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.proyecto.model.TicketDetail;

public interface TicketDetailRepository extends JpaRepository<TicketDetail, Integer> {
    // Encontrar detalle del ticket por id ticket
    @Query(value = "SELECT * FROM tickets_details WHERE id_ticket = :idTicket;", nativeQuery = true)
    List<TicketDetail> findByTicketId(@Param("idTicket") Integer idTicket);

    // Encontrar detalle del ticket por id product
    // @Query(value = "SELECT * FROM tickets_details WHERE id_product = :idProduct;", nativeQuery = true)
    // List<TicketDetail> findByProductId(@Param("idProduct") Integer idProduct);
}