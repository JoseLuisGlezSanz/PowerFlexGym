package com.example.proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.proyecto.model.Visit;

public interface VisitRepository extends JpaRepository<Visit, Integer> {
}