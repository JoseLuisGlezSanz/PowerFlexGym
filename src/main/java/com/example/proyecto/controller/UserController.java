package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import com.example.proyecto.dto.UserRequest;
import com.example.proyecto.dto.UserResponse;
import com.example.proyecto.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        List<UserResponse> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Integer id) {
        UserResponse user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody UserRequest userRequest) {
        UserResponse createdUser = userService.save(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Integer id, @RequestBody UserRequest userRequest) {
        UserResponse updatedUser = userService.update(id, userRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/email")
    public ResponseEntity<UserResponse> findByMail(@RequestParam String email) {
        UserResponse user = userService.findByMail(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/search/username")
    public ResponseEntity<UserResponse> findByUsername(@RequestParam String username) {
        UserResponse user = userService.findByUsername(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<UserResponse>> findByRoleId(@PathVariable Integer roleId) {
        List<UserResponse> users = userService.findByRoleId(roleId);
        return ResponseEntity.ok(users);
    }
}