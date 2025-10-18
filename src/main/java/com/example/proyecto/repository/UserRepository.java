package com.example.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.proyecto.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    // Buscar usuario por email
    @Query(value = "SELECT * FROM users WHERE LOWER(mail) = LOWER(:mail);", nativeQuery = true)
    List<User> findByMail(@Param("mail") String mail);
    
    // Buscar usuario por nombre de usuario
    @Query(value = "SELECT * FROM users WHERE LOWER(user) = LOWER(:user);", nativeQuery = true)
    List<User> findByUsername(@Param("user") String user);

    // Buscar usuario por rol de usuario
    @Query(value = "SELECT * FROM users WHERE id_role = :idRole;", nativeQuery = true)
    List<User> findByRoleId(@Param("idRole") String idRole);
}