package com.example.proyecto.service;

import java.util.List;

import com.example.proyecto.dto.EmergencyContactRequest;
import com.example.proyecto.dto.EmergencyContactResponse;

public interface EmergencyContactService {
    List<EmergencyContactResponse> findAll();
    EmergencyContactResponse findById(Integer id);
    EmergencyContactResponse save(EmergencyContactRequest emergencyContactRequest);
    EmergencyContactResponse update(Integer id, EmergencyContactRequest emergencyContactRequest);
    void delete(Integer id);
    EmergencyContactResponse findByIdCustomer(Integer idCustomer);
}