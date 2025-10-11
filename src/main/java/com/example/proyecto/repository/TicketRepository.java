package com.example.proyecto.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.proyecto.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    // Buscar tickets por usuario
    List<Ticket> findByUserIdUser(Integer idUser);
    
    // Buscar tickets por cliente
    List<Ticket> findByCustomerIdCustomer(Integer idCustomer);
    
    // Buscar tickets por estado
    List<Ticket> findByStatus(Integer status);
    
    // Buscar tickets por rango de fechas
    List<Ticket> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Buscar tickets por método de pago
    List<Ticket> findByMethodPayment(Integer methodPayment);
    
    // Obtener total de ventas por período
    @Query("SELECT SUM(t.total) FROM Ticket t WHERE t.date BETWEEN :startDate AND :endDate")
    Double findTotalSalesByDateRange(@Param("startDate") LocalDateTime startDate, 
                                    @Param("endDate") LocalDateTime endDate);
}