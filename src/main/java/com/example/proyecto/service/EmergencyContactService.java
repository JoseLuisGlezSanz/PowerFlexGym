package com.example.proyecto.service;

import java.util.List;

import com.example.proyecto.dto.EmergencyContactRequest;
import com.example.proyecto.dto.EmergencyContactResponse;

public interface EmergencyContactService {
    List<EmergencyContactResponse> findAll();
    EmergencyContactResponse findById(Integer id);
    EmergencyContactResponse update(Integer id, EmergencyContactRequest emergencyContactRequest);
    EmergencyContactResponse findByIdCustomer(Integer idCustomer);
    List<EmergencyContactResponse> getAll(int page, int pageSize);
}