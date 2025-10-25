package com.example.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.proyecto.model.CustomerMembership;
import com.example.proyecto.model.CustomerMembershipPk;

public interface CustomerMembershipRepository extends JpaRepository<CustomerMembership, CustomerMembershipPk> {
    // Buscar por cliente
    @Query(value = "SELECT * FROM customers_memberships WHERE id_customer = :idCustomer;", nativeQuery = true)
    List<CustomerMembership> findByCustomerId(@Param("idCustomer") Integer idCustomer);

    // Buscar por membresía
    @Query(value = "SELECT * FROM customers_memberships WHERE id_membership = :idMembership;", nativeQuery = true)
    List<CustomerMembership> findByMembershipId(@Param("idMembership") Integer idMembership);

    // Buscar membresia activa que expira pronto
    @Query(value = "SELECT * FROM customer_memberships WHERE membership_status = true AND expiration_date BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL 3 DAY;", nativeQuery = true)
    List<CustomerMembership> findActiveMembershipsExpiringSoon();
}