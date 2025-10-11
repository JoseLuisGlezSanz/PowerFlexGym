package com.example.proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.proyecto.model.Gym;

public interface GymRepository extends JpaRepository<Gym, Integer> {
}
