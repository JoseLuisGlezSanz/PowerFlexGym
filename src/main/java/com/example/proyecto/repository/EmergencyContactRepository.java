package com.example.proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.proyecto.model.EmergencyContact;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Integer> {
}
