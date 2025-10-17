package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import com.example.proyecto.dto.RoleRequest;
import com.example.proyecto.dto.RoleResponse;
import com.example.proyecto.service.RoleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<List<RoleResponse>> findAll() {
        List<RoleResponse> roles = roleService.findAll();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> findById(@PathVariable Integer id) {
        RoleResponse role = roleService.findById(id);
        return ResponseEntity.ok(role);
    }

    @PostMapping
    public ResponseEntity<RoleResponse> create(@RequestBody RoleRequest roleRequest) {
        RoleResponse createdRole = roleService.save(roleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> update(
            @PathVariable Integer id, 
            @RequestBody RoleRequest roleRequest) {
        RoleResponse updatedRole = roleService.update(id, roleRequest);
        return ResponseEntity.ok(updatedRole);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<RoleResponse>> findByStatus(@PathVariable Integer status) {
        List<RoleResponse> roles = roleService.findByStatus(status);
        return ResponseEntity.ok(roles);
    }
}
