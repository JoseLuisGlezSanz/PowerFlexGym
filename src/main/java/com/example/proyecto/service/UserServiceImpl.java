package com.example.proyecto.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.proyecto.dto.GymResponse;
import com.example.proyecto.dto.RoleResponse;
import com.example.proyecto.dto.UserRequest;
import com.example.proyecto.dto.UserResponse;
import com.example.proyecto.model.Gym;
import com.example.proyecto.model.Role;
import com.example.proyecto.model.User;
import com.example.proyecto.repository.GymRepository;
import com.example.proyecto.repository.RoleRepository;
import com.example.proyecto.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final GymRepository gymRepository;

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserResponse> findById(Integer id) {
        return userRepository.findById(id)
                .map(this::mapToResponse);
    }

    @Override
    public Optional<UserResponse> findByUsername(String username) {
        return userRepository.findByUser(username)
                .map(this::mapToResponse);
    }

    @Override
    public Optional<UserResponse> findByEmail(String email) {
        return userRepository.findByMail(email)
                .map(this::mapToResponse);
    }

    @Override
    public List<UserResponse> findByRoleId(Integer roleId) {
        return userRepository.findByRoleIdRole(roleId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> findByGymId(Integer gymId) {
        return userRepository.findByGymIdGym(gymId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> findByStatus(Integer status) {
        return userRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse create(UserRequest request) {
        Role role = roleRepository.findById(request.getIdRole())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        
        Gym gym = gymRepository.findById(request.getIdGym())
                .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado"));

        if (userRepository.existsByUser(request.getUser())) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }

        if (userRepository.existsByMail(request.getMail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        User user = User.builder()
                .user(request.getUser())
                .mail(request.getMail())
                .phone(request.getPhone())
                .name(request.getName())
                .password(request.getPassword())
                .status(request.getStatus())
                .role(role)
                .gym(gym)
                .build();

        User saved = userRepository.save(user);
        return mapToResponse(saved);
    }

    @Override
    public UserResponse update(Integer id, UserRequest request) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Role role = roleRepository.findById(request.getIdRole())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        
        Gym gym = gymRepository.findById(request.getIdGym())
                .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado"));

        if (!existing.getUser().equals(request.getUser()) && 
            userRepository.existsByUser(request.getUser())) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }

        if (!existing.getMail().equals(request.getMail()) && 
            userRepository.existsByMail(request.getMail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        existing.setUser(request.getUser());
        existing.setMail(request.getMail());
        existing.setPhone(request.getPhone());
        existing.setName(request.getName());
        existing.setPassword(request.getPassword());
        existing.setStatus(request.getStatus());
        existing.setRole(role);
        existing.setGym(gym);

        User updated = userRepository.save(existing);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return userRepository.existsById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUser(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByMail(email);
    }

    private UserResponse mapToResponse(User user) {
        RoleResponse roleResponse = RoleResponse.builder()
                .idRole(user.getRole().getIdRole())
                .role(user.getRole().getRole())
                .status(user.getRole().getStatus())
                .build();

        GymResponse gymResponse = GymResponse.builder()
                .idGym(user.getGym().getIdGym())
                .gym(user.getGym().getGym())
                .build();

        return UserResponse.builder()
                .idUser(user.getIdUser())
                .user(user.getUser())
                .mail(user.getMail())
                .phone(user.getPhone())
                .name(user.getName())
                .status(user.getStatus())
                .role(roleResponse)
                .gym(gymResponse)
                .build();
    } 
}