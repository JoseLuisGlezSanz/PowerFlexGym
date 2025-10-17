package com.example.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.proyecto.model.Membership;

public interface MembershipRepository extends JpaRepository<Membership, Integer> {
    // Buscar membresía por nombre
    List<Membership> findByMembership(String membershipName);
}