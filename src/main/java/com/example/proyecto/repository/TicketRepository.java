package com.example.proyecto.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.proyecto.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    // Buscar tickets por cliente
    @Query(value = "SELECT * FROM tickets WHERE customer_id = :customerId;", nativeQuery = true)
    List<Ticket> findAllTicketsByCustomerId(@Param("customerId") Long customerId);
    
    // Buscar tickets por usuario (vendedor)
    @Query(value = "SELECT * FROM tickets WHERE user_id = :userId;", nativeQuery = true)
    List<Ticket> findAllTicketsByUserId(@Param("userId") Long userId);

    // Buscar tickets por cliente paginado
    @Query(value = "SELECT * FROM tickets WHERE customer_id = :customerId;", nativeQuery = true)
    Page<Ticket> getAllTicketsByCustomerId(@Param("customerId") Long customerId, Pageable pageable);
    
    // Buscar tickets por usuario (vendedor) paginado
    @Query(value = "SELECT * FROM tickets WHERE user_id = :userId;", nativeQuery = true)
    Page<Ticket> getAllTicketsByUserId(@Param("userId") Long userId, Pageable pageable);
}