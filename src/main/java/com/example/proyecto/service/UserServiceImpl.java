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
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final GymRepository gymRepository;

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponse findById(Integer id) {
        User user = userRepository.findById(id)
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
        
        User savedUser = userRepository.save(user);
        return UserMapper.toResponse(savedUser);
    }

    @Override
    public UserResponse update(Integer id, UserRequest req) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        
        UserMapper.copyToEntity(req, existingUser);
        User updatedUser = userRepository.save(existingUser);
        return UserMapper.toResponse(updatedUser);
    }

    @Override
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserResponse findByMail(String mail) {
        return userRepository.findByMail(mail).stream()
                .findFirst()
                .map(UserMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + mail));
    }

    @Override
    public List<UserResponse> findByUsername(String user) {
        return userRepository.findByUsername(user).stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @Override
    public List<UserResponse> findByRoleId(Integer idRole) {
        return userRepository.findAll().stream()
                .map(UserMapper::toResponse)
                .toList();
    }
}