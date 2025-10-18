package com.example.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.proyecto.model.Membership;

public interface MembershipRepository extends JpaRepository<Membership, Integer> {
    // Buscar membresía por nombre
    @Query(value = "SELECT * FROM memberships WHERE LOWER(membership) = LOWER(:membership);", nativeQuery = true)
    List<Membership> findByMembership(@Param("membership") String membership);

    // Buscar membresía por estatus
    @Query(value = "SELECT * FROM memberships WHERE status = :status;", nativeQuery = true)
    List<Membership> findByStatus(@Param("status") Integer status);

    // Buscar membresía por ID gym
    @Query(value = "SELECT * FROM memberships WHERE id_gym = :idGym;", nativeQuery = true)
    List<Membership> findByGymId(@Param("idGym") Integer idGym);
}