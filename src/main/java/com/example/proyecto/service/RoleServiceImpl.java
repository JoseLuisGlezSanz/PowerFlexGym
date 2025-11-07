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
    private final RoleRepository roleRepository;

    @Override
    public List<RoleResponse> findAll() {
        return roleRepository.findAll().stream()
                .map(RoleMapper::toResponse)
                .toList();
    }

    @Override
    public RoleResponse findById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));
        return RoleMapper.toResponse(role);
    }

    @Override
    public RoleResponse create(RoleRequest roleRequest) {
        Role role = RoleMapper.toEntity(roleRequest);
        Role savedRole = roleRepository.save(role);
        return RoleMapper.toResponse(savedRole);
    }

    @Override
    public RoleResponse update(Long id, RoleRequest roleRequest) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));
        
        RoleMapper.copyToEntity(roleRequest, existingRole);
        Role updatedRole = roleRepository.save(existingRole);
        return RoleMapper.toResponse(updatedRole);
    }

    // @Override
    // public void delete(Integer id) {
    //     roleRepository.deleteById(id);
    // }

    @Override
    public List<RoleResponse> findByStatus(Integer statusValue) {
          List<Role> roles = roleRepository.findByStatus(statusValue);
          return roles.stream().map(RoleMapper::toResponse).toList();
    }
}
