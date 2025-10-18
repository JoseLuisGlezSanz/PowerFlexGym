package com.example.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.proyecto.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    // Buscar role por estatus
    @Query(value = "SELECT * FROM roles WHERE status = :status;", nativeQuery = true)
    List<Role> findByStatus(@Param("status") Integer status);
}