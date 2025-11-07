package com.example.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.proyecto.model.Membership;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    // Buscar membresía por nombre
    @Query(value = "SELECT * FROM memberships WHERE LOWER(name) = LOWER(:name);", nativeQuery = true)
    List<Membership> findMembershipByName(@Param("name") String name);

    // Buscar membresía por ID gym
    @Query(value = "SELECT * FROM memberships WHERE gym_id = :gymId;", nativeQuery = true)
    List<Membership> findMembershipByGymId(@Param("gymId") Long gymId);
}