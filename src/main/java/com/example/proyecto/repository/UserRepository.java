package com.example.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.proyecto.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    // Buscar usuario por email
    List<User> findByMail(String mail);
    
    // Buscar usuario por nombre de usuario
    List<User> findByUser(String userName);
}