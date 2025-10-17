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
    private final MembershipRepository membershipRepository;
    private final GymRepository gymRepository;

    @Override
    public List<MembershipResponse> findAll() {
        return membershipRepository.findAll().stream()
                .map(MembershipMapper::toResponse)
                .toList();
    }

    @Override
    public MembershipResponse findById(Integer id) {
        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membresía no encontrada con ID: " + id));
        return MembershipMapper.toResponse(membership);
    }

    @Override
    public MembershipResponse save(MembershipRequest req) {
        Gym gym = gymRepository.findById(req.getIdGym())
                .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado con ID: " + req.getIdGym()));
        
        Membership membership = MembershipMapper.toEntity(req);
        membership.setGym(gym);
        
        Membership savedMembership = membershipRepository.save(membership);
        return MembershipMapper.toResponse(savedMembership);
    }

    @Override
    public MembershipResponse update(Integer id, MembershipRequest req) {
        Membership existingMembership = membershipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membresía no encontrada con ID: " + id));
        
        MembershipMapper.copyToEntity(req, existingMembership);
        Membership updatedMembership = membershipRepository.save(existingMembership);
        return MembershipMapper.toResponse(updatedMembership);
    }

    @Override
    public void delete(Integer id) {
        membershipRepository.deleteById(id);
    }

    @Override
    public List<MembershipResponse> findByMembershipName(String membershipName) {
        return membershipRepository.findByMembership(membershipName).stream()
                .map(MembershipMapper::toResponse)
                .toList();
    }

    @Override
    public List<MembershipResponse> findByStatus(Integer status) {
        return membershipRepository.findAll().stream()
                .filter(m -> m.getStatus().equals(status))
                .map(MembershipMapper::toResponse)
                .toList();
    }

    @Override
    public List<MembershipResponse> findByGymId(Integer idGym) {
        return membershipRepository.findAll().stream()
                .filter(m -> m.getGym().getIdGym().equals(idGym))
                .map(MembershipMapper::toResponse)
                .toList();
    }
}