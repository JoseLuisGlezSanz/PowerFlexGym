package com.example.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.proyecto.model.Gym;

public interface GymRepository extends JpaRepository<Gym, Integer> {
    // Buscar gimnasio por nombre
    @Query(value = "SELECT * FROM gyms WHERE LOWER(gym) = LOWER(:gym);", nativeQuery = true)
    List<Gym> findByGym(@Param("gym") String gym);
}