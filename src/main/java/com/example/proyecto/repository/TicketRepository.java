package com.example.proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.proyecto.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
}
