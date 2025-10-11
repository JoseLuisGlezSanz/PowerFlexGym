package com.example.proyecto.service;

import java.util.List;
import com.example.proyecto.dto.EmergencyContactRequest;
import com.example.proyecto.dto.EmergencyContactResponse;

public interface EmergencyContactService {

    List<EmergencyContactResponse> findAll();

    EmergencyContactResponse findById(Integer id);

    EmergencyContactResponse create(EmergencyContactRequest request);

    EmergencyContactResponse update(Integer id, EmergencyContactRequest request);

    void delete(Integer id);
}
