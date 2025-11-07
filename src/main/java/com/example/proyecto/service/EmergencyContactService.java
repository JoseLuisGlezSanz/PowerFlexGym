package com.example.proyecto.service;

import java.util.List;

import com.example.proyecto.dto.EmergencyContactRequest;
import com.example.proyecto.dto.EmergencyContactResponse;

public interface EmergencyContactService {
    List<EmergencyContactResponse> findAll();
    EmergencyContactResponse findById(Long id);
    EmergencyContactResponse update(Long id, EmergencyContactRequest emergencyContactRequest);
    // void delete(Long id);
    List<EmergencyContactResponse> getAll(int page, int pageSize);
    EmergencyContactResponse findEmergencyContactByIdCustomer(Long customerId);
}