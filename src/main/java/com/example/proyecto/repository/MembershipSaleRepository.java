package com.example.proyecto.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.proyecto.model.MembershipSale;

public interface MembershipSaleRepository extends JpaRepository<MembershipSale, Integer> {

    // Buscar ventas por cliente
    List<MembershipSale> findByCustomerIdCustomer(Integer idCustomer);
    
    // Buscar ventas por usuario vendedor
    List<MembershipSale> findByUserIdUser(Integer idUser);
    
    // Buscar ventas por gimnasio
    List<MembershipSale> findByGymIdGym(Integer idGym);
    
    // Buscar ventas por membresía
    List<MembershipSale> findByMembershipIdMembership(Integer idMembership);
    
    // Buscar ventas por rango de fechas
    List<MembershipSale> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Buscar ventas activas (no canceladas)
    List<MembershipSale> findByCancellationFalse();
    
    // Total de ventas por período
    @Query("SELECT SUM(ms.payment) FROM MembershipSale ms WHERE ms.date BETWEEN :startDate AND :endDate AND ms.cancellation = false")
    Double findTotalSalesByDateRange(@Param("startDate") LocalDateTime startDate, 
                                    @Param("endDate") LocalDateTime endDate);
}