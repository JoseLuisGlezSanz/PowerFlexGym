package com.example.proyecto.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.proyecto.model.Visit;

public interface VisitRepository extends JpaRepository<Visit, Integer> {

    // Buscar visitas por cliente
    List<Visit> findByCustomerIdCustomer(Integer customerId);
    
    // Buscar visitas por gimnasio
    List<Visit> findByGymIdGym(Integer idGym);
    
    // Buscar visitas pendientes
    List<Visit> findByPending(Integer pending);
    
    // Buscar visitas por rango de fechas
    List<Visit> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Contar visitas por cliente en un período
    @Query("SELECT COUNT(v) FROM Visit v WHERE v.customer.idCustomer = :customerId AND v.date BETWEEN :startDate AND :endDate")
    Long countVisitsByCustomerAndDateRange(@Param("customerId") Integer customerId,
                                          @Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);
    
    // Buscar visitas pendientes por gimnasio
    List<Visit> findByGymIdGymAndPending(Integer idGym, Integer pending);
}