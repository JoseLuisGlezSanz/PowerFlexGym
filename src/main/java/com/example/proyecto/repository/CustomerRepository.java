package com.example.proyecto.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.proyecto.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    
    // Buscar cliente por teléfono
    Optional<Customer> findByPhone(String phone);
    
    // Buscar clientes por gimnasio
    List<Customer> findByGymIdGym(Integer idGym);
    
    // Buscar clientes por nombre (búsqueda parcial)
    List<Customer> findByNameContainingIgnoreCase(String name);
    
    // Contar clientes por gimnasio
    Long countByGymIdGym(Integer idGym);
}