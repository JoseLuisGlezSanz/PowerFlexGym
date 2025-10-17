package com.example.proyecto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.proyecto.dto.RoleRequest;
import com.example.proyecto.dto.RoleResponse;
import com.example.proyecto.mapper.RoleMapper;
import com.example.proyecto.model.Role;
import com.example.proyecto.repository.RoleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService{
    private final RoleRepository repository;

    @Override
    public List<RoleResponse> findAll() {
        return repository.findAll().stream()
                .map(RoleMapper::toResponse)
                .toList();
    }

    @Override
    public RoleResponse findById(Integer id) {
        Role role = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));
        return RoleMapper.toResponse(role);
    }

    @Override
    public RoleResponse save(RoleRequest req) {
        Role role = RoleMapper.toEntity(req);
        Role savedRole = repository.save(role);
        return RoleMapper.toResponse(savedRole);
    }

    @Override
    public RoleResponse update(Integer id, RoleRequest req) {
        Role existingRole = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));
        
        RoleMapper.copyToEntity(req, existingRole);
        Role updatedRole = repository.save(existingRole);
        return RoleMapper.toResponse(updatedRole);
    }

    @Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Rol no encontrado con ID: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<RoleResponse> findByStatus(Integer status) {
        // Implementar cuando agregues el método al repository
        return repository.findAll().stream()
                .filter(r -> r.getStatus().equals(status))
                .map(RoleMapper::toResponse)
                .toList();
    }
}
