package com.example.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.proyecto.model.MembershipSale;

public interface MembershipSaleRepository extends JpaRepository<MembershipSale, Integer> {
    // Buscar ventas por cliente
    List<MembershipSale> findByCustomerIdCustomer(Integer idCustomer);
    
    // Buscar ventas por gimnasio
    List<MembershipSale> findByGymIdGym(Integer idGym); 
}