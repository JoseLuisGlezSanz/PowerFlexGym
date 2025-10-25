package com.example.proyecto.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.proyecto.model.MembershipSale;

public interface MembershipSaleRepository extends JpaRepository<MembershipSale, Integer> {
    // Buscar ventas por usuario (vendedor)
    @Query(value = "SELECT * FROM memberships_sales WHERE id_user = :idUser;", nativeQuery = true)
    List<MembershipSale> findByUserId(@Param("idUser") Integer idUser);

    // Buscar ventas por cliente
    @Query(value = "SELECT * FROM memberships_sales WHERE id_customer = :idCustomer;", nativeQuery = true)
    List<MembershipSale> findByCustomerId(@Param("idCustomer") Integer idCustomer);

    // Buscar ventas por Gym
    @Query(value = "SELECT * FROM memberships_sales WHERE id_gym = :idGym;", nativeQuery = true)
    List<MembershipSale> findByGymId(@Param("idGym") Integer idGym);

    // Buscar ventas por usuario (vendedor) paginado
    @Query(value = "SELECT * FROM memberships_sales WHERE id_user = :idUser", nativeQuery = true)
    Page<MembershipSale> findByUserId(@Param("idUser") Integer idUser, Pageable pageable);

    // Buscar ventas por Gym paginado
    @Query(value = "SELECT * FROM memberships_sales WHERE id_gym = :idGym", nativeQuery = true)
    Page<MembershipSale> findByGymId(@Param("idGym") Integer idGym, Pageable pageable);
}