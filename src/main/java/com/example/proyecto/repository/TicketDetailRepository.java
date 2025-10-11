package com.example.proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.proyecto.model.TicketDetail;

public interface TicketDetailRepository extends JpaRepository<TicketDetail, Integer> {
}
