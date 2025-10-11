package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.example.proyecto.dto.GymRequest;
import com.example.proyecto.dto.GymResponse;
import com.example.proyecto.service.GymService;

@RestController
@RequestMapping("/api/gyms")
public class GymController {

    @Autowired
    private GymService gymService;

    @GetMapping
    public List<GymResponse> getAll() {
        return gymService.findAll();
    }

    @GetMapping("/{id}")
    public GymResponse getById(@PathVariable Integer id) {
        return gymService.findById(id);
    }

    @PostMapping
    public GymResponse create(@RequestBody GymRequest request) {
        return gymService.create(request);
    }

    @PutMapping("/{id}")
    public GymResponse update(@PathVariable Integer id, @RequestBody GymRequest request) {
        return gymService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        gymService.delete(id);
    }
}
