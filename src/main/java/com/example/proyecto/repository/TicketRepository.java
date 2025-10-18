package com.example.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.proyecto.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    // Buscar tickets por cliente
    @Query(value = "SELECT * FROM tickets WHERE id_customer = :idCustomer;", nativeQuery = true)
    List<Ticket> findByCustomerId(@Param("idCustomer") Integer idCustomer);
    
    // Buscar tickets por usuario (vendedor)
    @Query(value = "SELECT * FROM tickets WHERE id_user = :idUser;", nativeQuery = true)
    List<Ticket> findByUserId(@Param("idUser") Integer idUser);
}