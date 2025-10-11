package com.example.proyecto.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.proyecto.dto.MembershipSaleRequest;
import com.example.proyecto.dto.MembershipSaleResponse;
import com.example.proyecto.model.Customer;
import com.example.proyecto.model.Gym;
import com.example.proyecto.model.Membership;
import com.example.proyecto.model.MembershipSale;
import com.example.proyecto.model.User;
import com.example.proyecto.repository.CustomerRepository;
import com.example.proyecto.repository.GymRepository;
import com.example.proyecto.repository.MembershipRepository;
import com.example.proyecto.repository.MembershipSaleRepository;
import com.example.proyecto.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MembershipSaleServiceImpl implements MembershipSaleService{
    private final MembershipSaleRepository membershipSaleRepository;
    private final CustomerRepository customerRepository;
    private final MembershipRepository membershipRepository;
    private final GymRepository gymRepository;
    private final UserRepository userRepository;

    @Override
    public List<MembershipSaleResponse> findAll() {
        return membershipSaleRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MembershipSaleResponse> findById(Integer id) {
        return membershipSaleRepository.findById(id)
                .map(this::mapToResponse);
    }

    @Override
    public List<MembershipSaleResponse> findByCustomerId(Integer customerId) {
        return membershipSaleRepository.findByCustomerIdCustomer(customerId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MembershipSaleResponse> findByUserId(Integer userId) {
        return membershipSaleRepository.findByUserIdUser(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MembershipSaleResponse> findByGymId(Integer gymId) {
        return membershipSaleRepository.findByGymIdGym(gymId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MembershipSaleResponse> findByMembershipId(Integer membershipId) {
        return membershipSaleRepository.findByMembershipIdMembership(membershipId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MembershipSaleResponse> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return membershipSaleRepository.findByDateBetween(startDate, endDate).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MembershipSaleResponse> findActiveSales() {
        return membershipSaleRepository.findByCancellationFalse().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MembershipSaleResponse create(MembershipSaleRequest request) {
        Customer customer = null;
        if (request.getIdCustomer() != null) {
            customer = customerRepository.findById(request.getIdCustomer())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        }
        
        Membership membership = membershipRepository.findById(request.getIdMembership())
                .orElseThrow(() -> new RuntimeException("Membresía no encontrada"));
        
        Gym gym = gymRepository.findById(request.getIdGym())
                .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado"));
        
        User user = userRepository.findById(request.getIdUser())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        MembershipSale membershipSale = MembershipSale.builder()
                .date(request.getDate() != null ? request.getDate() : LocalDateTime.now())
                .payment(request.getPayment())
                .cancellation(request.getCancellation() != null ? request.getCancellation() : false)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .customer(customer)
                .membership(membership)
                .gym(gym)
                .user(user)
                .build();

        MembershipSale saved = membershipSaleRepository.save(membershipSale);
        return mapToResponse(saved);
    }

    @Override
    public MembershipSaleResponse update(Integer id, MembershipSaleRequest request) {
        MembershipSale existing = membershipSaleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta de membresía no encontrada"));

        Customer customer = null;
        if (request.getIdCustomer() != null) {
            customer = customerRepository.findById(request.getIdCustomer())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        }
        
        Membership membership = membershipRepository.findById(request.getIdMembership())
                .orElseThrow(() -> new RuntimeException("Membresía no encontrada"));
        
        Gym gym = gymRepository.findById(request.getIdGym())
                .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado"));
        
        User user = userRepository.findById(request.getIdUser())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        existing.setDate(request.getDate());
        existing.setPayment(request.getPayment());
        existing.setCancellation(request.getCancellation());
        existing.setStartDate(request.getStartDate());
        existing.setEndDate(request.getEndDate());
        existing.setCustomer(customer);
        existing.setMembership(membership);
        existing.setGym(gym);
        existing.setUser(user);

        MembershipSale updated = membershipSaleRepository.save(existing);
        return mapToResponse(updated);
    }

    @Override
    public void delete(Integer id) {
        if (!membershipSaleRepository.existsById(id)) {
            throw new RuntimeException("Venta de membresía no encontrada");
        }
        membershipSaleRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return membershipSaleRepository.existsById(id);
    }

    @Override
    public Double getTotalSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        Double total = membershipSaleRepository.findTotalSalesByDateRange(startDate, endDate);
        return total != null ? total : 0.0;
    }

    @Override
    public MembershipSaleResponse cancelSale(Integer id) {
        MembershipSale sale = membershipSaleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta de membresía no encontrada"));

        sale.setCancellation(true);
        MembershipSale updated = membershipSaleRepository.save(sale);
        return mapToResponse(updated);
    }

    private MembershipSaleResponse mapToResponse(MembershipSale membershipSale) {
        return MembershipSaleResponse.builder()
                .idMembershipSale(membershipSale.getIdMembershipSales())
                .date(membershipSale.getDate())
                .payment(membershipSale.getPayment())
                .cancellation(membershipSale.getCancellation())
                .startDate(membershipSale.getStartDate())
                .endDate(membershipSale.getEndDate())
                .idMembership(membershipSale.getMembership().getIdMembership())
                .idCustomer(membershipSale.getCustomer() != null ? membershipSale.getCustomer().getIdCustomer() : null)
                .idGym(membershipSale.getGym().getIdGym())
                .idUser(membershipSale.getUser().getIdUser())
                .build();
    }
}
