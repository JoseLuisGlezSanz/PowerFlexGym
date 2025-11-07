package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;
import com.example.proyecto.dto.RoleRequest;
import com.example.proyecto.dto.RoleResponse;
import com.example.proyecto.model.Role;
import com.example.proyecto.service.RoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Tag(name = "Roles", description = "Provides methods for managing roles")
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    @Operation(summary = "Get all roles")
    @ApiResponse(responseCode = "200", description = "List of all roles", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Role.class)))})
    public List<RoleResponse> findAll() {
        return roleService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get role by ID")
    @ApiResponse(responseCode = "200", description = "Role by ID", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Role.class)))})
    public RoleResponse findById(@PathVariable Long id) {
        return roleService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new role")
    @ApiResponse(responseCode = "200", description = "Role creation", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Role.class)))})
    public ResponseEntity<RoleResponse> create(@RequestBody RoleRequest roleRequest) {
        RoleResponse createdRole = roleService.create(roleRequest);
        return ResponseEntity
                .created(URI.create("/api/v1/roles/" + createdRole.getId()))
                .body(createdRole);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update role by ID")
    @ApiResponse(responseCode = "200", description = "Role update", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Role.class)))})
    public RoleResponse update(@PathVariable Long id, @RequestBody RoleRequest roleRequest) {
        return roleService.update(id, roleRequest);
    }

    // @DeleteMapping("/{id}")
    // @Operation(summary = "Delete role by ID")
    // @ApiResponse(responseCode = "200", description = "Role deleted successfully", 
    //         content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Role.class)))})
    // public ResponseEntity<Void> delete(@PathVariable Integer id) {
    //     roleService.delete(id);
    //     return ResponseEntity.noContent().build();
    // }

    @GetMapping("/{status}")
    @Operation(summary = "Get roles by status")
    @ApiResponse(responseCode = "200", description = "List of roles with the specified status",
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Role.class)))})
    public List<RoleResponse> findByStatus(@PathVariable Integer statusValue) {
        List<RoleResponse> roles = roleService.findByStatus(statusValue);
        return roles;
    }
}