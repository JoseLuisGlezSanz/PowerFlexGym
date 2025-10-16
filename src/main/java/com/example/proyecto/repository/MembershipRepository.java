package com.example.proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.proyecto.model.Membership;

public interface MembershipRepository extends JpaRepository<Membership, Integer> {
}