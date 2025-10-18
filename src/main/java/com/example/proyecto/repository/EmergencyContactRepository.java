package com.example.proyecto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.proyecto.model.EmergencyContact;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Integer> {
    // Buscar contacto por id cliente
    @Query(value = "SELECT * FROM emergencys_contacts WHERE id_customer = :idCustomer;", nativeQuery = true)
    Optional<EmergencyContact> findByIdCustomer(@Param("idCustomer") Integer idCustomer);
}