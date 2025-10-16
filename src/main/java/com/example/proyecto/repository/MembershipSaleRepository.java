package com.example.proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.proyecto.model.MembershipSale;

public interface MembershipSaleRepository extends JpaRepository<MembershipSale, Integer> {
}