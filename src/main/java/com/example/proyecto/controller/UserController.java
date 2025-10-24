package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import com.example.proyecto.dto.UserRequest;
import com.example.proyecto.dto.UserResponse;
import com.example.proyecto.model.User;
import com.example.proyecto.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Provides methods for managing users")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "200", description = "List of all users", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class)))})
    public ResponseEntity<List<UserResponse>> findAll() {
        List<UserResponse> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    @ApiResponse(responseCode = "200", description = "User found", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class)))})
    public ResponseEntity<UserResponse> findById(@PathVariable Integer id) {
        UserResponse user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    @Operation(summary = "Create a new user")
    @ApiResponse(responseCode = "200", description = "User created successfully", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class)))})
    public ResponseEntity<UserResponse> create(@RequestBody UserRequest userRequest) {
        UserResponse createdUser = userService.save(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user by ID")
    @ApiResponse(responseCode = "200", description = "User updated successfully", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class)))})
    public ResponseEntity<UserResponse> update(@PathVariable Integer id, @RequestBody UserRequest userRequest) {
        UserResponse updatedUser = userService.update(id, userRequest);
        return ResponseEntity.ok(updatedUser);
    }

    // @DeleteMapping("/{id}")
    // @Operation(summary = "Delete user by ID")
    // @ApiResponse(responseCode = "200", description = "User deleted successfully", 
    //         content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class)))})
    // public ResponseEntity<Void> delete(@PathVariable Integer id) {
    //     userService.delete(id);
    //     return ResponseEntity.noContent().build();
    // }

    @GetMapping("/search/email/{mail}")
    @Operation(summary = "Get user by email")
    @ApiResponse(responseCode = "200", description = "User found by email", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class)))})
    public UserResponse findByMail(@PathVariable String mail) {
        return userService.findByMail(mail);
    }

    @GetMapping("/search/username/{user}")
    @Operation(summary = "Get user by username")
    @ApiResponse(responseCode = "200", description = "User found by username", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class)))})
    public List<UserResponse> findByUsername(@PathVariable String user) {
        return userService.findByUsername(user);
    }

    @GetMapping("/role/{idRole}")
    @Operation(summary = "Get users by role ID")
    @ApiResponse(responseCode = "200", description = "List of users with the specified role", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class)))})
    public List<UserResponse> findByRoleId(@PathVariable Integer idRole) {
        return userService.findByRoleId(idRole);
    }

    @GetMapping(value = "paginationAll", params = { "page", "pageSize" })
    @Operation(summary = "Get all users with pagination")
    public List<UserResponse> getAllPaginated(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        List<UserResponse> users = userService.getAll(page, pageSize);
        return users;
    }

    @GetMapping("/gym/{idGym}")
    @Operation(summary = "Get users by gym ID")
    @ApiResponse(responseCode = "200", description = "List of users with the specified gym", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class)))})
    public List<UserResponse> findByGymId(@PathVariable Integer idGym) {
        return userService.findByGymId(idGym);
    }
}