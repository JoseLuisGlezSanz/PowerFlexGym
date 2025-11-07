package com.example.proyecto.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        return UserMapper.toResponse(user);
    }

    @Override
    public UserResponse create(UserRequest userRequest) {
        Role role = roleRepository.findById(userRequest.getRoleId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + userRequest.getRoleId()));

        Gym gym = gymRepository.findById(userRequest.getGymId())
                .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado con ID: " + userRequest.getGymId()));
        
        User user = UserMapper.toEntity(userRequest, role, gym);   
        
        User savedUser = userRepository.save(user);
        return UserMapper.toResponse(savedUser);
    }

    @Override
    public UserResponse update(Long id, UserRequest userRequest) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        Role role = roleRepository.findById(userRequest.getRoleId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + userRequest.getRoleId()));

        Gym gym = gymRepository.findById(userRequest.getGymId())
                .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado con ID: " + userRequest.getGymId()));
        
        UserMapper.copyToEntity(userRequest, existingUser, role, gym);

        User updatedUser = userRepository.save(existingUser);
        return UserMapper.toResponse(updatedUser);
    }

    // @Override
    // public void delete(Long id) {
    //     userRepository.deleteById(id);
    // }

    public List<UserResponse> getAll(int page, int pageSize) {
        PageRequest pageReq = PageRequest.of(page, pageSize);
        Page<User> users = userRepository.findAll(pageReq);
        return users.getContent().stream().map(UserMapper::toResponse).toList();
    }

    @Override
    public UserResponse findByMail(String mail) {
        User user = userRepository.findByMail(mail);
        return UserMapper.toResponse(user);
    }

    @Override
    public UserResponse findByUsername(String nameUser) {
        User user = userRepository.findByUserName(nameUser);
        return UserMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> findByRoleId(Long roleId) {
          List<User> users = userRepository.findByRoleId(roleId);
          return users.stream().map(UserMapper::toResponse).toList();
    }

    @Override
    public List<UserResponse> findByGymId(Long gymId) {
          List<User> users = userRepository.findByGymId(gymId);
          return users.stream().map(UserMapper::toResponse).toList();
    }
}