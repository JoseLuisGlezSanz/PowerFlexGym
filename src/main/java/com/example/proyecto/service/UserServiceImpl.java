package com.example.proyecto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.proyecto.dto.UserRequest;
import com.example.proyecto.dto.UserResponse;
import com.example.proyecto.mapper.UserMapper;
import com.example.proyecto.model.Gym;
import com.example.proyecto.model.Role;
import com.example.proyecto.model.User;
import com.example.proyecto.repository.GymRepository;
import com.example.proyecto.repository.RoleRepository;
import com.example.proyecto.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final GymRepository gymRepository;

    @Override
    public List<UserResponse> findAll() {
        return repository.findAll().stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponse findById(Integer id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        return UserMapper.toResponse(user);
    }

    @Override
    public UserResponse save(UserRequest req) {
        Role role = roleRepository.findById(req.getIdRole())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + req.getIdRole()));
        Gym gym = gymRepository.findById(req.getIdGym())
                .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado con ID: " + req.getIdGym()));
        
        User user = UserMapper.toEntity(req);
        user.setRole(role);
        user.setGym(gym);
        
        User savedUser = repository.save(user);
        return UserMapper.toResponse(savedUser);
    }

    @Override
    public UserResponse update(Integer id, UserRequest req) {
        User existingUser = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        
        if (!existingUser.getRole().getIdRole().equals(req.getIdRole())) {
            Role role = roleRepository.findById(req.getIdRole())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + req.getIdRole()));
            existingUser.setRole(role);
        }
        
        if (!existingUser.getGym().getIdGym().equals(req.getIdGym())) {
            Gym gym = gymRepository.findById(req.getIdGym())
                    .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado con ID: " + req.getIdGym()));
            existingUser.setGym(gym);
        }
        
        UserMapper.copyToEntity(req, existingUser);
        User updatedUser = repository.save(existingUser);
        return UserMapper.toResponse(updatedUser);
    }

    @Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public UserResponse findByMail(String mail) {
        return repository.findByMail(mail).stream()
                .findFirst()
                .map(UserMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + mail));
    }

    @Override
    public UserResponse findByUsername(String username) {
        return repository.findByUser(username).stream()
                .findFirst()
                .map(UserMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con username: " + username));
    }

    @Override
    public List<UserResponse> findByRoleId(Integer idRole) {
        // Implementar cuando agregues el método al repository
        return repository.findAll().stream()
                .filter(u -> u.getRole().getIdRole().equals(idRole))
                .map(UserMapper::toResponse)
                .toList();
    }
}