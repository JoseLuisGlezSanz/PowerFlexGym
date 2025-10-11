package com.example.proyecto.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.proyecto.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    // Buscar usuario por nombre de usuario
    Optional<User> findByUser(String user);
    
    // Buscar usuario por email
    Optional<User> findByMail(String mail);
    
    // Buscar usuarios por rol
    List<User> findByRoleIdRole(Integer idRole);
    
    // Buscar usuarios por gimnasio
    List<User> findByGymIdGym(Integer idGym);
    
    // Buscar usuarios por estado
    List<User> findByStatus(Integer status);
    
    // Verificar existencia por nombre de usuario
    Boolean existsByUser(String user);
    
    // Verificar existencia por email
    Boolean existsByMail(String mail);
}