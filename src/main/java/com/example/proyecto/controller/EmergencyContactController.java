package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

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
@RequestMapping("/api/v1/emergencys-contacts")
@RequiredArgsConstructor
@Tag(name = "EmergencyContact", description = "Provides methods for managing emergency contact")
public class EmergencyContactController {
    private final EmergencyContactService emergencyContactService;

    @GetMapping
    @Operation(summary = "Get all emergency contacts")
    @ApiResponse(responseCode = "200", description = "List of all emergency contacts", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EmergencyContact.class)))})
    public List<EmergencyContactResponse> findAll() {
        return emergencyContactService.findAll();
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get emergency contact by customer ID")
    @ApiResponse(responseCode = "200", description = "Emergency contact by customer ID", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EmergencyContact.class)))})
    public EmergencyContactResponse findByIdCustomer(@PathVariable Long customerId) {
        return emergencyContactService.findEmergencyContactByIdCustomer(customerId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get emergency contact by ID")
    @ApiResponse(responseCode = "200", description = "Emergency contact by ID", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EmergencyContact.class)))})
    public EmergencyContactResponse findById(@PathVariable Long id) {
        return emergencyContactService.findById(id);
    }

    // @PostMapping
    // @Operation(summary = "Create a new emergency contact")
    // @ApiResponse(responseCode = "200", description = "Emergency contact created",
    //         content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EmergencyContact.class)))})
    // public EmergencyContactResponse create(@RequestBody EmergencyContactRequest contactRequest) {
    //     return emergencyContactService.save(contactRequest);
    // }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update emergency contact by ID")
    @ApiResponse(responseCode = "200", description = "Emergency contact update", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EmergencyContact.class)))})
    public EmergencyContactResponse update(@PathVariable Long id, @RequestBody EmergencyContactRequest emergencyContactRequest) {
        return emergencyContactService.update(id, emergencyContactRequest);
    }

    // @DeleteMapping("/{id}")
    // @Operation(summary = "Delete emergency contact by ID")
    // @ApiResponse(responseCode = "200", description = "Emergency contact deleted successfully",
    //         content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EmergencyContact.class)))})
    // public ResponseEntity<Void> delete(@PathVariable Integer id) {
    //     emergencyContactService.delete(id);
    //     return ResponseEntity.noContent().build();
    // }

    @GetMapping(value = "paginationAll", params = { "page", "pageSize" })
    @Operation(summary = "Get all emegency contacts with pagination")
    public List<EmergencyContactResponse> getAllPaginated(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        List<EmergencyContactResponse> emergencyContacts = emergencyContactService.getAll(page, pageSize);
        return emergencyContacts;
    }
}