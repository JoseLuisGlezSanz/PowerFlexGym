package com.example.proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.proyecto.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
