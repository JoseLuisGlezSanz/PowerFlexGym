package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import com.example.proyecto.dto.GymRequest;
import com.example.proyecto.dto.GymResponse;
import com.example.proyecto.service.GymService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/gyms")
@RequiredArgsConstructor
public class GymController {
    private final GymService gymService;

    @GetMapping
    public ResponseEntity<List<GymResponse>> getAllGyms() {
        List<GymResponse> gyms = gymService.findAll();
        return ResponseEntity.ok(gyms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GymResponse> getGymById(@PathVariable Integer id) {
        return gymService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<GymResponse> createGym(@Valid @RequestBody GymRequest request) {
        GymResponse created = gymService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GymResponse> updateGym(
            @PathVariable Integer id,
            @Valid @RequestBody GymRequest request) {
        GymResponse updated = gymService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGym(@PathVariable Integer id) {
        gymService.delete(id);
        return ResponseEntity.noContent().build();
    }
}