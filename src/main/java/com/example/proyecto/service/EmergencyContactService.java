package com.example.proyecto.service;

import java.util.List;
import java.util.Optional;

import com.example.proyecto.dto.EmergencyContactRequest;
import com.example.proyecto.dto.EmergencyContactResponse;

public interface EmergencyContactService {
    List<EmergencyContactResponse> findAll();
    Optional<EmergencyContactResponse> findById(Integer id);
    Optional<EmergencyContactResponse> findByCustomerId(Integer customerId);
    EmergencyContactResponse create(EmergencyContactRequest request);
    EmergencyContactResponse update(Integer id, EmergencyContactRequest request);
    void delete(Integer id);
    boolean existsById(Integer id);
    boolean existsByCustomerId(Integer customerId);
}