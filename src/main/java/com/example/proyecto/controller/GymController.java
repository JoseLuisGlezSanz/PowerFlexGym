package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import com.example.proyecto.dto.GymRequest;
import com.example.proyecto.dto.GymResponse;
import com.example.proyecto.model.Gym;
import com.example.proyecto.service.GymService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/gyms")
@RequiredArgsConstructor
@Tag(name = "Gyms", description = "Provides methods for managing gyms")
public class GymController {
    private final GymService gymService;

    @GetMapping
    @Operation(summary = "Get all gyms")
    @ApiResponse(responseCode = "200", description = "List of all gyms", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Gym.class)))})
    public ResponseEntity<List<GymResponse>> findAll() {
        List<GymResponse> gyms = gymService.findAll();
        return ResponseEntity.ok(gyms);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get gym by ID")
    @ApiResponse(responseCode = "200", description = "Gym found", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Gym.class)))})
    public ResponseEntity<GymResponse> findById(@PathVariable Integer id) {
        GymResponse gym = gymService.findById(id);
        return ResponseEntity.ok(gym);
    }

    @PostMapping
    @Operation(summary = "Create a new gym")
    @ApiResponse(responseCode = "200", description = "Gym created successfully", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Gym.class)))})
    public ResponseEntity<GymResponse> create(@RequestBody GymRequest gymRequest) {
        GymResponse createdGym = gymService.save(gymRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGym);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update gym by ID")
    @ApiResponse(responseCode = "200", description = "Gym updated successfully", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Gym.class)))})
    public ResponseEntity<GymResponse> update(@PathVariable Integer id, @RequestBody GymRequest gymRequest) {
        GymResponse updatedGym = gymService.update(id, gymRequest);
        return ResponseEntity.ok(updatedGym);
    }

    // @DeleteMapping("/{id}")
    // @Operation(summary = "Delete gym by ID")
    // @ApiResponse(responseCode = "200", description = "Gym deleted successfully", 
    //         content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Gym.class)))})
    // public ResponseEntity<Void> delete(@PathVariable Integer id) {
    //     gymService.delete(id);
    //     return ResponseEntity.noContent().build();
    // }

    @GetMapping("/search/{gym}")
    @Operation(summary = "Get gyms by name")
    @ApiResponse(responseCode = "200", description = "List of gyms matching the name", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Gym.class)))})
    public List<GymResponse> findByGym(@RequestParam String gym) {
        return gymService.findByGym(gym);
    }

    @GetMapping(value = "paginationAll", params = { "page", "pageSize" })
    @Operation(summary = "Get all gyms with pagination")
    public List<GymResponse> getAllPaginated(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        List<GymResponse> gyms = gymService.getAll(page, pageSize);
        return gyms;
    }
}