package com.example.proyecto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.proyecto.dto.MembershipSaleRequest;
import com.example.proyecto.dto.MembershipSaleResponse;
import com.example.proyecto.mapper.MembershipSaleMapper;
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
    private final MembershipSaleRepository repository;
    private final CustomerRepository customerRepository;
    private final MembershipRepository membershipRepository;
    private final GymRepository gymRepository;
    private final UserRepository userRepository;

    @Override
    public List<MembershipSaleResponse> findAll() {
        return repository.findAll().stream()
                .map(MembershipSaleMapper::toResponse)
                .toList();
    }

    @Override
    public MembershipSaleResponse findById(Integer id) {
        MembershipSale membershipSale = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta de membresía no encontrada con ID: " + id));
        return MembershipSaleMapper.toResponse(membershipSale);
    }

    @Override
    public MembershipSaleResponse save(MembershipSaleRequest req) {
        Customer customer = customerRepository.findById(req.getIdCustomer())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + req.getIdCustomer()));
        Membership membership = membershipRepository.findById(req.getIdMembership())
                .orElseThrow(() -> new RuntimeException("Membresía no encontrada con ID: " + req.getIdMembership()));
        Gym gym = gymRepository.findById(req.getIdGym())
                .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado con ID: " + req.getIdGym()));
        User user = userRepository.findById(req.getIdUser())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + req.getIdUser()));
        
        MembershipSale membershipSale = MembershipSaleMapper.toEntity(req);
        membershipSale.setCustomer(customer);
        membershipSale.setMembership(membership);
        membershipSale.setGym(gym);
        membershipSale.setUser(user);
        
        MembershipSale savedMembershipSale = repository.save(membershipSale);
        return MembershipSaleMapper.toResponse(savedMembershipSale);
    }

    @Override
    public MembershipSaleResponse update(Integer id, MembershipSaleRequest req) {
        MembershipSale existingMembershipSale = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta de membresía no encontrada con ID: " + id));
        
        if (!existingMembershipSale.getCustomer().getIdCustomer().equals(req.getIdCustomer())) {
            Customer customer = customerRepository.findById(req.getIdCustomer())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + req.getIdCustomer()));
            existingMembershipSale.setCustomer(customer);
        }
        
        if (!existingMembershipSale.getMembership().getIdMembership().equals(req.getIdMembership())) {
            Membership membership = membershipRepository.findById(req.getIdMembership())
                    .orElseThrow(() -> new RuntimeException("Membresía no encontrada con ID: " + req.getIdMembership()));
            existingMembershipSale.setMembership(membership);
        }
        
        if (!existingMembershipSale.getGym().getIdGym().equals(req.getIdGym())) {
            Gym gym = gymRepository.findById(req.getIdGym())
                    .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado con ID: " + req.getIdGym()));
            existingMembershipSale.setGym(gym);
        }
        
        if (!existingMembershipSale.getUser().getIdUser().equals(req.getIdUser())) {
            User user = userRepository.findById(req.getIdUser())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + req.getIdUser()));
            existingMembershipSale.setUser(user);
        }
        
        MembershipSaleMapper.copyToEntity(req, existingMembershipSale);
        MembershipSale updatedMembershipSale = repository.save(existingMembershipSale);
        return MembershipSaleMapper.toResponse(updatedMembershipSale);
    }

    @Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Venta de membresía no encontrada con ID: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<MembershipSaleResponse> findByCustomerId(Integer idCustomer) {
        return repository.findByCustomerIdCustomer(idCustomer).stream()
                .map(MembershipSaleMapper::toResponse)
                .toList();
    }

    @Override
    public List<MembershipSaleResponse> findByGymId(Integer idGym) {
        return repository.findByGymIdGym(idGym).stream()
                .map(MembershipSaleMapper::toResponse)
                .toList();
    }

    @Override
    public List<MembershipSaleResponse> findNotCancelled() {
        // Implementar cuando agregues el método al repository
        return repository.findAll().stream()
                .filter(ms -> !ms.getCancellation())
                .map(MembershipSaleMapper::toResponse)
                .toList();
    }
}