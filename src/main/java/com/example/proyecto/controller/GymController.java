package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import com.example.proyecto.dto.GymRequest;
import com.example.proyecto.dto.GymResponse;
import com.example.proyecto.service.GymService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/gyms")
@RequiredArgsConstructor
@Tag(name = "Gyms", description = "Provides methods for managing gyms")
public class GymController {
    private final GymService gymService;

    @GetMapping
    public ResponseEntity<List<GymResponse>> findAll() {
        List<GymResponse> gyms = gymService.findAll();
        return ResponseEntity.ok(gyms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GymResponse> findById(@PathVariable Integer id) {
        GymResponse gym = gymService.findById(id);
        return ResponseEntity.ok(gym);
    }

    @PostMapping
    public ResponseEntity<GymResponse> create(@RequestBody GymRequest gymRequest) {
        GymResponse createdGym = gymService.save(gymRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGym);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GymResponse> update(@PathVariable Integer id, @RequestBody GymRequest gymRequest) {
        GymResponse updatedGym = gymService.update(id, gymRequest);
        return ResponseEntity.ok(updatedGym);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        gymService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<GymResponse>> findByGymName(@RequestParam String name) {
        List<GymResponse> gyms = gymService.findByGymName(name);
        return ResponseEntity.ok(gyms);
    }
}