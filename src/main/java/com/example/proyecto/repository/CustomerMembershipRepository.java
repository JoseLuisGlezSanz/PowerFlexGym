package com.example.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.proyecto.model.CustomerMembership;

    public interface CustomerMembershipRepository extends JpaRepository<CustomerMembership, Integer> {
        // Buscar por cliente
        List<CustomerMembership> findByCustomerIdCustomer(Integer idCustomer);
        
        // Buscar por estado de membresía
        List<CustomerMembership> findByMembershipStatus(Boolean status);

        // Buscar por cliente y estado
        List<CustomerMembership> findByCustomerIdCustomerAndMembershipStatus(Integer idCustomer, Boolean status);
    }