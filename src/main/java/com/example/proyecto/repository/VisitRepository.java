package com.example.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.proyecto.model.Visit;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    // Buscar visitas por cliente
    @Query(value = "SELECT * FROM visits WHERE customer_id = :customerId;", nativeQuery = true)
    List<Visit> findByCustomerId(@Param("customerId") Long customerId);
    
    // Buscar visitas por gimnasio
    @Query(value = "SELECT * FROM visits WHERE gym_id = :gymId;", nativeQuery = true)
    List<Visit> findByGymId(@Param("gymId") Long gymId);
}