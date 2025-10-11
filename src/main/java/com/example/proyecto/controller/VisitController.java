package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.example.proyecto.dto.VisitRequest;
import com.example.proyecto.dto.VisitResponse;
import com.example.proyecto.service.VisitService;

@RestController
@RequestMapping("/api/visits")
public class VisitController {

    @Autowired
    private VisitService visitService;

    @GetMapping
    public List<VisitResponse> getAll() {
        return visitService.findAll();
    }

    @GetMapping("/{id}")
    public VisitResponse getById(@PathVariable Integer id) {
        return visitService.findById(id);
    }

    @PostMapping
    public VisitResponse create(@RequestBody VisitRequest request) {
        return visitService.create(request);
    }

    @PutMapping("/{id}")
    public VisitResponse update(@PathVariable Integer id, @RequestBody VisitRequest request) {
        return visitService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        visitService.delete(id);
    }
}
