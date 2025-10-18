package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import com.example.proyecto.dto.EmergencyContactRequest;
import com.example.proyecto.dto.EmergencyContactResponse;
import com.example.proyecto.service.EmergencyContactService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/emergency-contacts")
@RequiredArgsConstructor
@Tag(name = "EmergencyContact", description = "Provides methods for managing emergency contact")
public class EmergencyContactController {
    private final EmergencyContactService emergencyContactService;

    @GetMapping
    public ResponseEntity<List<EmergencyContactResponse>> findAll() {
        List<EmergencyContactResponse> contacts = emergencyContactService.findAll();
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmergencyContactResponse> findById(@PathVariable Integer id) {
        EmergencyContactResponse contact = emergencyContactService.findById(id);
        return ResponseEntity.ok(contact);
    }

    @PostMapping
    public ResponseEntity<EmergencyContactResponse> create(@RequestBody EmergencyContactRequest contactRequest) {
        EmergencyContactResponse createdContact = emergencyContactService.save(contactRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdContact);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmergencyContactResponse> update(@PathVariable Integer id, @RequestBody EmergencyContactRequest contactRequest) {
        EmergencyContactResponse updatedContact = emergencyContactService.update(id, contactRequest);
        return ResponseEntity.ok(updatedContact);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        emergencyContactService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<EmergencyContactResponse> findByIdCustomer(@PathVariable Integer customerId) {
        EmergencyContactResponse contact = emergencyContactService.findByIdCustomer(customerId);
        return ResponseEntity.ok(contact);
    }
}