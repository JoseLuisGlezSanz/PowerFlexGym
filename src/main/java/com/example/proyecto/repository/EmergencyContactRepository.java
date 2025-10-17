package com.example.proyecto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.proyecto.model.EmergencyContact;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Integer> {
    // Buscar contacto por cliente
    Optional<EmergencyContact> findByCustomerIdCustomer(Integer idCustomer);
}