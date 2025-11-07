package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
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
    @ApiResponse(responseCode = "200", description = "List of all users", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class)))})
    public List<UserResponse> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    @ApiResponse(responseCode = "200", description = "User by ID", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class)))})
    public UserResponse findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new user")
    @ApiResponse(responseCode = "200", description = "User create", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class)))})
    public ResponseEntity<UserResponse> create(@RequestBody UserRequest userRequest) {
        UserResponse createdUser = userService.create(userRequest);
        return ResponseEntity
                .created(URI.create("/api/v1/users/" + createdUser.getId()))
                .body(createdUser);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update user by ID")
    @ApiResponse(responseCode = "200", description = "User update", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class)))})
    public UserResponse update(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        return userService.update(id, userRequest);
    }

    // @DeleteMapping("/{id}")
    // @Operation(summary = "Delete user by ID")
    // @ApiResponse(responseCode = "200", description = "User delete", content = {
    //      @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class)))})
    // public Void delete(@PathVariable Long id) {
    //     return ResponseEntity.noContent().build();
    // }

    @GetMapping(value = "paginationAll", params = { "page", "pageSize" })
    @Operation(summary = "Get all users with pagination")
    @ApiResponse(responseCode = "200", description = "List of all users paginated", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class)))})
    public List<UserResponse> getAllPaginated(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        List<UserResponse> users = userService.getAll(page, pageSize);
        return users;
    }

    @GetMapping("/{mail}")
    @Operation(summary = "Get user by mail")
    @ApiResponse(responseCode = "200", description = "User by mail", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class)))})
    public UserResponse findByMail(@PathVariable String mail) {
        return userService.findByMail(mail);
    }

    @GetMapping("/{nameUser}")
    @Operation(summary = "Get user by username")
    @ApiResponse(responseCode = "200", description = "User by username", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class)))})
    public UserResponse findByUsername(@PathVariable String nameUser) {
        return userService.findByUsername(nameUser);
    }

    @GetMapping("/{roleId}")
    @Operation(summary = "Get users by role ID")
    @ApiResponse(responseCode = "200", description = "List of users by roleId", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class)))})
    public List<UserResponse> findByRoleId(@PathVariable Long roleId) {
        return userService.findByRoleId(roleId);
    }

    @GetMapping("/{gymId}")
    @Operation(summary = "Get users by gym ID")
    @ApiResponse(responseCode = "200", description = "List of users by gymId", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class)))})
    public List<UserResponse> findByGymId(@PathVariable Long gymId) {
        return userService.findByGymId(gymId);
    }
}