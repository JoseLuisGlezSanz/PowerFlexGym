package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import com.example.proyecto.dto.EmergencyContactRequest;
import com.example.proyecto.dto.EmergencyContactResponse;
import com.example.proyecto.model.EmergencyContact;
import com.example.proyecto.service.EmergencyContactService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/emergency-contacts")
@RequiredArgsConstructor
@Tag(name = "EmergencyContact", description = "Provides methods for managing emergency contact")
public class EmergencyContactController {
    private final EmergencyContactService emergencyContactService;

    @GetMapping
    @Operation(summary = "Get all emergency contacts")
    @ApiResponse(responseCode = "200", description = "List of all emergency contacts", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EmergencyContact.class)))})
    public ResponseEntity<List<EmergencyContactResponse>> findAll() {
        List<EmergencyContactResponse> contacts = emergencyContactService.findAll();
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get emergency contact by ID")
    @ApiResponse(responseCode = "200", description = "Emergency contact found", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EmergencyContact.class)))})
    public ResponseEntity<EmergencyContactResponse> findById(@PathVariable Integer id) {
        EmergencyContactResponse contact = emergencyContactService.findById(id);
        return ResponseEntity.ok(contact);
    }

    @PostMapping
    @Operation(summary = "Create a new emergency contact")
    @ApiResponse(responseCode = "200", description = "Emergency contact created successfully",
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EmergencyContact.class)))})
    public ResponseEntity<EmergencyContactResponse> create(@RequestBody EmergencyContactRequest contactRequest) {
        EmergencyContactResponse createdContact = emergencyContactService.save(contactRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdContact);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update emergency contact by ID")
    @ApiResponse(responseCode = "200", description = "Emergency contact updated successfully",
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EmergencyContact.class)))})
    public ResponseEntity<EmergencyContactResponse> update(@PathVariable Integer id, @RequestBody EmergencyContactRequest contactRequest) {
        EmergencyContactResponse updatedContact = emergencyContactService.update(id, contactRequest);
        return ResponseEntity.ok(updatedContact);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete emergency contact by ID")
    @ApiResponse(responseCode = "200", description = "Emergency contact deleted successfully",
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EmergencyContact.class)))})
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        emergencyContactService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{idCustomer}")
    @Operation(summary = "Get emergency contact by customer ID")
    @ApiResponse(responseCode = "200", description = "Emergency contact found for the customer",
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EmergencyContact.class)))})
    public EmergencyContactResponse findByIdCustomer(@PathVariable Integer idCustomer) {
        return emergencyContactService.findByIdCustomer(idCustomer);
    }
}