package com.example.proyecto.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.proyecto.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {    
    // Buscar clientes por nombre
    @Query(value = "SELECT * FROM customers WHERE LOWER(name) = LOWER(:name);", nativeQuery = true)
    List<Customer> findByName(@Param("name") String name);
    
    // Buscar clientes con número verificado
    @Query(value = "SELECT * FROM customers WHERE verified_number = true", nativeQuery = true)
    List<Customer> findByVerifiedNumberTrue(Sort sort);

    // Buscar clientes con número verificado paginado
    @Query(value = "SELECT * FROM customers WHERE verified_number = true", nativeQuery = true)
    Page<Customer> findByVerifiedNumberTrue(Pageable pageable);

    // Buscar clientes por gymnasio
    @Query(value = "SELECT * FROM customers WHERE id_gym = :idGym;", nativeQuery = true)
    List<Customer> findByGymId(@Param("idGym") Integer idGym);

    // Buscar clientes por gymnasio paginado
    @Query(value = "SELECT * FROM customers WHERE id_gym = :idGym", nativeQuery = true)
    Page<Customer> findByGymId(@Param("idGym") Integer idGym,Pageable pageable);
}