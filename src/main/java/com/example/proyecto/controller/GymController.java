package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
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
    @ApiResponse(responseCode = "200", description = "List of all gyms", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Gym.class)))})
    public List<GymResponse> findAll() {
        return gymService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get gym by ID")
    @ApiResponse(responseCode = "200", description = "Gym by ID", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Gym.class)))})
    public GymResponse findById(@PathVariable Long id) {
        return gymService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new gym")
    @ApiResponse(responseCode = "200", description = "Gym creation", content = {
        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Gym.class)))})
    public ResponseEntity<GymResponse> create(@RequestBody GymRequest gymRequest) {
        GymResponse createdGym = gymService.create(gymRequest);
        return ResponseEntity
                .created(URI.create("/api/v1/gyms/" + createdGym.getId()))
                .body(createdGym);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update gym by ID")
    @ApiResponse(responseCode = "200", description = "Gym update", content = {
        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Gym.class)))})
    public GymResponse update(@PathVariable Long id, @RequestBody GymRequest gymRequest) {
        return gymService.update(id, gymRequest);
    }

    // @DeleteMapping("/{id}")
    // @Operation(summary = "Delete gym by ID")
    // @ApiResponse(responseCode = "200", description = "Gym deleted", 
    //         content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Gym.class)))})
    // public Void delete(@PathVariable Long id) {
    //     return gymService.delete(id);
    // }

    @GetMapping(value = "paginationAll", params = { "page", "pageSize" })
    @Operation(summary = "Get all gyms with pagination")
    @ApiResponse(responseCode = "200", description = "List of gyms paginated", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Gym.class)))})
    public List<GymResponse> getAllPaginated(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        List<GymResponse> gyms = gymService.getAll(page, pageSize);
        return gyms;
    }

    @GetMapping("/{name}")
    @Operation(summary = "Get gyms by name")
    @ApiResponse(responseCode = "200", description = "Gym by name", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Gym.class)))})
    public GymResponse findByNameGym(@PathVariable String name) {
        return gymService.findByName(name);
    }
}