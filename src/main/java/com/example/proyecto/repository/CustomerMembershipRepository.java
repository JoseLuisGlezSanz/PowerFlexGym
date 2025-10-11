package com.example.proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.proyecto.model.CustomerMembership;
import com.example.proyecto.model.CustomerMembershipId;

public interface CustomerMembershipRepository extends JpaRepository<CustomerMembership, CustomerMembershipId> {
}
