package com.example.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.proyecto.model.Gym;

public interface GymRepository extends JpaRepository<Gym, Integer> {
    // Buscar gimnasio por nombre
    List<Gym> findByGym(String gymName);
}