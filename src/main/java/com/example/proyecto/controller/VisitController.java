package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;
import com.example.proyecto.dto.VisitRequest;
import com.example.proyecto.dto.VisitResponse;
import com.example.proyecto.model.Visit;
import com.example.proyecto.service.VisitService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/visits")
@RequiredArgsConstructor
@Tag(name = "Visits", description = "Provides methods for managing visits")
public class VisitController {
    private final VisitService visitService;

    @GetMapping
    @Operation(summary = "Get all visits")
    @ApiResponse(responseCode = "200", description = "List of all visits", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Visit.class)))})
    public List<VisitResponse> findAll() {
        return visitService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get visit by ID")
    @ApiResponse(responseCode = "200", description = "Visit by ID", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Visit.class)))})
    public VisitResponse findById(@PathVariable Long id) {
        return visitService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new visit")
    @ApiResponse(responseCode = "200", description = "Visit create", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Visit.class)))})
    public ResponseEntity<VisitResponse> create(@RequestBody VisitRequest visitRequest) {
        VisitResponse createdVisit = visitService.create(visitRequest);
        return ResponseEntity
                .created(URI.create("/api/v1/visits/" + createdVisit.getId()))
                .body(createdVisit);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update visit by ID")
    @ApiResponse(responseCode = "200", description = "Visit updated successfully", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Visit.class)))})
    public VisitResponse update(@PathVariable Long id, @RequestBody VisitRequest visitRequest) {
        return visitService.update(id, visitRequest);
    }

    // @DeleteMapping("/{id}")
    // @Operation(summary = "Delete visit by ID")
    // @ApiResponse(responseCode = "200", description = "Visit deleted successfully", 
    //         content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Visit.class)))})
    // public ResponseEntity<Void> delete(@PathVariable Integer id) {
    //     visitService.delete(id);
    //     return ResponseEntity.noContent().build();
    // }

    @GetMapping("/{customerId}")
    @Operation(summary = "Get visits by customer ID")
    @ApiResponse(responseCode = "200", description = "List of visits by customer ID", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Visit.class)))})
    public List<VisitResponse> findByCustomerId(@PathVariable Long customerId) {
        return visitService.findByCustomerId(customerId);
    }

    @GetMapping("/{gymId}")
    @Operation(summary = "Get visits by gym ID")
    @ApiResponse(responseCode = "200", description = "List of visits by gym ID", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Visit.class)))})
    public List<VisitResponse> findByGymId(@PathVariable Long gymId) {
        return visitService.findByGymId(gymId);
    }
}