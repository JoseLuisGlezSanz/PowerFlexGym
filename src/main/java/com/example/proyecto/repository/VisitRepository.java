package com.example.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.proyecto.model.Visit;

public interface VisitRepository extends JpaRepository<Visit, Integer> {
    // Buscar visitas por cliente
    List<Visit> findByCustomerIdCustomer(Integer idCustomer);
    
    // Buscar visitas por gimnasio
    List<Visit> findByGymIdGym(Integer idGym);
}