package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.example.proyecto.dto.RoleRequest;
import com.example.proyecto.dto.RoleResponse;
import com.example.proyecto.service.RoleService;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public List<RoleResponse> getAll() {
        return roleService.findAll();
    }

    @GetMapping("/{id}")
    public RoleResponse getById(@PathVariable Integer id) {
        return roleService.findById(id);
    }

    @PostMapping
    public RoleResponse create(@RequestBody RoleRequest request) {
        return roleService.create(request);
    }

    @PutMapping("/{id}")
    public RoleResponse update(@PathVariable Integer id, @RequestBody RoleRequest request) {
        return roleService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        roleService.delete(id);
    }
}
