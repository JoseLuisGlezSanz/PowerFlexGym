package com.example.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.proyecto.model.CustomerMembership;
import com.example.proyecto.model.CustomerMembershipPk;

public interface CustomerMembershipRepository extends JpaRepository<CustomerMembership, CustomerMembershipPk> {
    // Buscar por cliente
    @Query(value = "SELECT * FROM customers_memberships WHERE customer_id = :customerId;", nativeQuery = true)
    List<CustomerMembership> findByCustomerId(@Param("customerId") Long customerId);

    // Buscar por membresía
    @Query(value = "SELECT * FROM customers_memberships WHERE membership_id = :membershipId;", nativeQuery = true)
    List<CustomerMembership> findByMembershipId(@Param("membershipId") Long membershipId);

    // Buscar por gym
    @Query(value = "SELECT * FROM customers_memberships WHERE gym_id = :gymId;", nativeQuery = true)
    List<CustomerMembership> findByGymId(@Param("gymId") Long gymId);
}