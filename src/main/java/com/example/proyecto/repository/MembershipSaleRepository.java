package com.example.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.proyecto.model.MembershipSale;

public interface MembershipSaleRepository extends JpaRepository<MembershipSale, Integer> {
    // Buscar ventas por cliente
    @Query(value = "SELECT * FROM memberships_sales WHERE id_customer = :idCustomer;", nativeQuery = true)
    List<MembershipSale> findByCustomerId(@Param("idCustomer") Integer idCustomer);
    
    // Buscar ventas por gimnasio
    @Query(value = "SELECT * FROM memberships_sales WHERE id_gym = :idGym;", nativeQuery = true)
    List<MembershipSale> findByGymId(@Param("idGym") Integer idGym);

    // Buscar ventas no canceladas
    @Query(value = "SELECT * FROM membership_sales WHERE cancellation = false;", nativeQuery = true)
    List<MembershipSale> findNotCancelled();
}