package com.example.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.proyecto.model.Visit;

public interface VisitRepository extends JpaRepository<Visit, Integer> {
    // Buscar visitas por cliente
    @Query(value = "SELECT * FROM visits WHERE id_customer = :idCustomer;", nativeQuery = true)
    List<Visit> findByCustomerId(@Param("idCustomer") Integer idCustomer);
    
    // Buscar visitas por gimnasio
    @Query(value = "SELECT * FROM visits WHERE id_gym = :idGym;", nativeQuery = true)
    List<Visit> findByGymId(@Param("idGym") Integer idGym);
}