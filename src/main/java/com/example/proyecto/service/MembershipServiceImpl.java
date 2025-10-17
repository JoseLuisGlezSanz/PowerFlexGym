package com.example.proyecto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.proyecto.dto.MembershipRequest;
import com.example.proyecto.dto.MembershipResponse;
import com.example.proyecto.mapper.MembershipMapper;
import com.example.proyecto.model.Gym;
import com.example.proyecto.model.Membership;
import com.example.proyecto.repository.GymRepository;
import com.example.proyecto.repository.MembershipRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MembershipServiceImpl implements MembershipService{
    private final MembershipRepository repository;
    private final GymRepository gymRepository;

    @Override
    public List<MembershipResponse> findAll() {
        return repository.findAll().stream()
                .map(MembershipMapper::toResponse)
                .toList();
    }

    @Override
    public MembershipResponse findById(Integer id) {
        Membership membership = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membresía no encontrada con ID: " + id));
        return MembershipMapper.toResponse(membership);
    }

    @Override
    public MembershipResponse save(MembershipRequest req) {
        Gym gym = gymRepository.findById(req.getIdGym())
                .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado con ID: " + req.getIdGym()));
        
        Membership membership = MembershipMapper.toEntity(req);
        membership.setGym(gym);
        
        Membership savedMembership = repository.save(membership);
        return MembershipMapper.toResponse(savedMembership);
    }

    @Override
    public MembershipResponse update(Integer id, MembershipRequest req) {
        Membership existingMembership = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membresía no encontrada con ID: " + id));
        
        if (!existingMembership.getGym().getIdGym().equals(req.getIdGym())) {
            Gym gym = gymRepository.findById(req.getIdGym())
                    .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado con ID: " + req.getIdGym()));
            existingMembership.setGym(gym);
        }
        
        MembershipMapper.copyToEntity(req, existingMembership);
        Membership updatedMembership = repository.save(existingMembership);
        return MembershipMapper.toResponse(updatedMembership);
    }

    @Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Membresía no encontrada con ID: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<MembershipResponse> findByMembershipName(String membershipName) {
        return repository.findByMembership(membershipName).stream()
                .map(MembershipMapper::toResponse)
                .toList();
    }

    @Override
    public List<MembershipResponse> findByStatus(Integer status) {
        // Implementar cuando agregues el método al repository
        return repository.findAll().stream()
                .filter(m -> m.getStatus().equals(status))
                .map(MembershipMapper::toResponse)
                .toList();
    }

    @Override
    public List<MembershipResponse> findByGymId(Integer idGym) {
        // Implementar cuando agregues el método al repository
        return repository.findAll().stream()
                .filter(m -> m.getGym().getIdGym().equals(idGym))
                .map(MembershipMapper::toResponse)
                .toList();
    }
}