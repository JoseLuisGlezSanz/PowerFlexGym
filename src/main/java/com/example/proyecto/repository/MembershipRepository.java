package com.example.proyecto.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.proyecto.model.Membership;

public interface MembershipRepository extends JpaRepository<Membership, Integer> {

    // Buscar membresías por gimnasio
    List<Membership> findByGymIdGym(Integer idGym);
    
    // Buscar membresías por estado
    List<Membership> findByStatus(Integer status);
    
    // Buscar membresía por nombre y gimnasio
    Optional<Membership> findByMembershipAndGymIdGym(String membership, Integer idGym);
    
    // Buscar membresías activas por gimnasio
    List<Membership> findByGymIdGymAndStatus(Integer idGym, Integer status);
}