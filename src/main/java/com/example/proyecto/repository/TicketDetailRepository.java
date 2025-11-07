package com.example.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.proyecto.model.TicketDetail;

public interface TicketDetailRepository extends JpaRepository<TicketDetail, Long> {
    // Encontrar detalle del ticket por id ticket
    @Query(value = "SELECT * FROM tickets_details WHERE ticket_id = :ticketId;", nativeQuery = true)
    List<TicketDetail> findByTicketId(@Param("ticketId") Long ticketId);
}