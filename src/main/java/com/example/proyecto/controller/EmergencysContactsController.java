package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import com.example.proyecto.dto.EmergencyContactRequest;
import com.example.proyecto.dto.EmergencyContactResponse;
import com.example.proyecto.service.EmergencyContactService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/emergency-contacts")
@RequiredArgsConstructor
public class EmergencysContactsController {
    private final EmergencyContactService emergencyContactService;

    @GetMapping
    public ResponseEntity<List<EmergencyContactResponse>> getAllEmergencyContacts() {
        List<EmergencyContactResponse> contacts = emergencyContactService.findAll();
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmergencyContactResponse> getEmergencyContactById(@PathVariable Integer id) {
        return emergencyContactService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<EmergencyContactResponse> getEmergencyContactByCustomer(@PathVariable Integer customerId) {
        return emergencyContactService.findByCustomerId(customerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EmergencyContactResponse> createEmergencyContact(@Valid @RequestBody EmergencyContactRequest request) {
        EmergencyContactResponse created = emergencyContactService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmergencyContactResponse> updateEmergencyContact(
            @PathVariable Integer id,
            @Valid @RequestBody EmergencyContactRequest request) {
        EmergencyContactResponse updated = emergencyContactService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmergencyContact(@PathVariable Integer id) {
        emergencyContactService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{customerId}/exists")
    public ResponseEntity<Boolean> checkEmergencyContactExists(@PathVariable Integer customerId) {
        boolean exists = emergencyContactService.existsByCustomerId(customerId);
        return ResponseEntity.ok(exists);
    }
}