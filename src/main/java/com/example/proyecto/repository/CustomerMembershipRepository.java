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
    // Buscar por estado de membresía
    @Query(value = "SELECT * FROM customers_memberships WHERE membership_status = :status;", nativeQuery = true)
    List<CustomerMembership> findByMembershipStatus(@Param("status") Boolean status);

    // Buscar por cliente y estado
    @Query(value = "SELECT * FROM customers_memberships WHERE id_customer = :idCustomer AND membership_status = :status;", nativeQuery = true)
    List<CustomerMembership> findByCustomerIdCustomerAndMembershipStatus(@Param("idCustomer") Integer idCustomer, @Param("status") Boolean status);

    // Buscar membresia activa que expira pronto
    @Query(value = "SELECT * FROM customer_memberships WHERE membership_status = true AND expiration_date BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL 3 DAY;", nativeQuery = true)
    List<CustomerMembership> findActiveMembershipsExpiringSoon();
}