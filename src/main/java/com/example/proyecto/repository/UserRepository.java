package com.example.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.proyecto.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    // Buscar usuario por email
    @Query(value = "SELECT * FROM users WHERE LOWER(mail) = LOWER(:mail);", nativeQuery = true)
    User findByMail(@Param("mail") String mail);
    
    // Buscar usuario por nombre de usuario
    @Query(value = "SELECT * FROM users WHERE LOWER(user_name) = LOWER(:nameUser);", nativeQuery = true)
    User findByUserName(@Param("nameUser") String nameUser);

    // Buscar usuario por rol de usuario
    @Query(value = "SELECT * FROM users WHERE role_id = :roleId;", nativeQuery = true)
    List<User> findByRoleId(@Param("roleId") Long roleId);

    //Buscar usuario por gym
    @Query(value = "SELECT * FROM users WHERE gym_id = :gymId;", nativeQuery = true)
    List<User> findByGymId(@Param("gymId") Long gymId);   
}