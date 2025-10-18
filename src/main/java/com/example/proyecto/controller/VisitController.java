package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
    @ApiResponse(responseCode = "200", description = "List of all visits", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Visit.class)))})
    public ResponseEntity<List<VisitResponse>> findAll() {
        List<VisitResponse> visits = visitService.findAll();
        return ResponseEntity.ok(visits);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get visit by ID")
    @ApiResponse(responseCode = "200", description = "Visit found", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Visit.class)))})
    public ResponseEntity<VisitResponse> findById(@PathVariable Integer id) {
        VisitResponse visit = visitService.findById(id);
        return ResponseEntity.ok(visit);
    }

    @PostMapping
    @Operation(summary = "Create a new visit")
    @ApiResponse(responseCode = "200", description = "Visit created successfully", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Visit.class)))})
    public ResponseEntity<VisitResponse> create(@RequestBody VisitRequest visitRequest) {
        VisitResponse createdVisit = visitService.save(visitRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVisit);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update visit by ID")
    @ApiResponse(responseCode = "200", description = "Visit updated successfully", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Visit.class)))})
    public ResponseEntity<VisitResponse> update(@PathVariable Integer id, @RequestBody VisitRequest visitRequest) {
        VisitResponse updatedVisit = visitService.update(id, visitRequest);
        return ResponseEntity.ok(updatedVisit);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete visit by ID")
    @ApiResponse(responseCode = "200", description = "Visit deleted successfully", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Visit.class)))})
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        visitService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{idCustomer}")
    @Operation(summary = "Get visits by customer ID")
    @ApiResponse(responseCode = "200", description = "List of visits for the specified customer", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Visit.class)))})
    public List<VisitResponse> findByCustomerId(@PathVariable Integer idCustomer) {
        return visitService.findByCustomerId(idCustomer);
    }

    @GetMapping("/gym/{idGym}")
    @Operation(summary = "Get visits by gym ID")
    @ApiResponse(responseCode = "200", description = "List of visits for the specified gym", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Visit.class)))})
    public List<VisitResponse> findByGymId(@PathVariable Integer idGym) {
        return visitService.findByGymId(idGym);
    }
}