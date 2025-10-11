package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.example.proyecto.dto.EmergencyContactRequest;
import com.example.proyecto.dto.EmergencyContactResponse;
import com.example.proyecto.service.EmergencyContactService;

@RestController
@RequestMapping("/api/emergency-contacts")
public class EmergencysContactsController {

    @Autowired
    private EmergencyContactService emergencyContactService;

    @GetMapping
    public List<EmergencyContactResponse> getAll() {
        return emergencyContactService.findAll();
    }

    @GetMapping("/{id}")
    public EmergencyContactResponse getById(@PathVariable Integer id) {
        return emergencyContactService.findById(id);
    }

    @PostMapping
    public EmergencyContactResponse create(@RequestBody EmergencyContactRequest request) {
        return emergencyContactService.create(request);
    }

    @PutMapping("/{id}")
    public EmergencyContactResponse update(@PathVariable Integer id, @RequestBody EmergencyContactRequest request) {
        return emergencyContactService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        emergencyContactService.delete(id);
    }
}
