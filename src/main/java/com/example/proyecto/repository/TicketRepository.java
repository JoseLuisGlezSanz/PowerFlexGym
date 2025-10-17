package com.example.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.proyecto.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    // Buscar tickets por cliente
    List<Ticket> findByCustomerIdCustomer(Integer idCustomer);
    
    // Buscar tickets por usuario (vendedor)
    List<Ticket> findByUserIdUser(Integer idUser);
}