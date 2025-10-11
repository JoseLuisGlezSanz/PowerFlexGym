package com.example.proyecto.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.proyecto.dto.RoleRequest;
import com.example.proyecto.dto.RoleResponse;
import com.example.proyecto.model.Role;
import com.example.proyecto.repository.RoleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService{
    private final RoleRepository roleRepository;

    @Override
    public List<RoleResponse> findAll() {
        return roleRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RoleResponse> findById(Integer id) {
        return roleRepository.findById(id)
                .map(this::mapToResponse);
    }

    @Override
    public List<RoleResponse> findByStatus(Integer status) {
        return roleRepository.findAll().stream()
                .filter(role -> role.getStatus().equals(status))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RoleResponse create(RoleRequest request) {
        Role role = Role.builder()
                .role(request.getRole())
                .status(request.getStatus())
                .build();

        Role saved = roleRepository.save(role);
        return mapToResponse(saved);
    }

    @Override
    public RoleResponse update(Integer id, RoleRequest request) {
        Role existing = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        existing.setRole(request.getRole());
        existing.setStatus(request.getStatus());

        Role updated = roleRepository.save(existing);
        return mapToResponse(updated);
    }

    @Override
    public void delete(Integer id) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Rol no encontrado");
        }
        roleRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return roleRepository.existsById(id);
    }

    private RoleResponse mapToResponse(Role role) {
        return RoleResponse.builder()
                .idRole(role.getIdRole())
                .role(role.getRole())
                .status(role.getStatus())
                .build();
    }
}
