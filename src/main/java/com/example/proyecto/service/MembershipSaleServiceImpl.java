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
    private final MembershipSaleRepository membershipSaleRepository;
    private final CustomerRepository customerRepository;
    private final MembershipRepository membershipRepository;
    private final GymRepository gymRepository;
    private final UserRepository userRepository;

    @Override
    public List<MembershipSaleResponse> findAll() {
        return membershipSaleRepository.findAll().stream()
                .map(MembershipSaleMapper::toResponse)
                .toList();
    }

    @Override
    public MembershipSaleResponse findById(Integer id) {
        MembershipSale membershipSale = membershipSaleRepository.findById(id)
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
        
        MembershipSale savedMembershipSale = membershipSaleRepository.save(membershipSale);
        return MembershipSaleMapper.toResponse(savedMembershipSale);
    }

    @Override
    public MembershipSaleResponse update(Integer id, MembershipSaleRequest req) {
        MembershipSale existingMembershipSale = membershipSaleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta de membresía no encontrada con ID: " + id));
        
        MembershipSaleMapper.copyToEntity(req, existingMembershipSale);
        MembershipSale updatedMembershipSale = membershipSaleRepository.save(existingMembershipSale);
        return MembershipSaleMapper.toResponse(updatedMembershipSale);
    }

    @Override
    public void delete(Integer id) {
        membershipSaleRepository.deleteById(id);
    }

    @Override
    public List<MembershipSaleResponse> findByCustomerId(Integer idCustomer) {
        return membershipSaleRepository.findByCustomerIdCustomer(idCustomer).stream()
                .map(MembershipSaleMapper::toResponse)
                .toList();
    }

    @Override
    public List<MembershipSaleResponse> findByGymId(Integer idGym) {
        return membershipSaleRepository.findByGymIdGym(idGym).stream()
                .map(MembershipSaleMapper::toResponse)
                .toList();
    }

    @Override
    public List<MembershipSaleResponse> findNotCancelled() {
        return membershipSaleRepository.findAll().stream()
                .filter(ms -> !ms.getCancellation())
                .map(MembershipSaleMapper::toResponse)
                .toList();
    }
}