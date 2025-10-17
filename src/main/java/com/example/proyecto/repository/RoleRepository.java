package com.example.proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.proyecto.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}