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
    public MembershipResponse findById(Long id) {
        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membresía no encontrada con ID: " + id));

        return MembershipMapper.toResponse(membership);
    }

    @Override
    public MembershipResponse create(MembershipRequest membershipRequest) {
        Gym gym = gymRepository.findById(membershipRequest.getGymId())
                .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado con ID: " + membershipRequest.getGymId()));
        
        Membership membership = MembershipMapper.toEntity(membershipRequest, gym);
       
        Membership savedMembership = membershipRepository.save(membership);
        return MembershipMapper.toResponse(savedMembership);
    }

    @Override
    public MembershipResponse update(Long id, MembershipRequest membershipRequest) {
        Membership existingMembership = membershipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membresía no encontrada con ID: " + id));

        Gym gym = gymRepository.findById(membershipRequest.getGymId())
            .orElseThrow(() -> new RuntimeException("Gym no encontrado con ID: " + membershipRequest.getGymId()));
        
        MembershipMapper.copyToEntity(membershipRequest, existingMembership, gym);

        Membership updatedMembership = membershipRepository.save(existingMembership);
        return MembershipMapper.toResponse(updatedMembership);
    }

    // @Override
    // public void delete(Integer id) {
    //     membershipRepository.deleteById(id);
    // }

    @Override
    public List<MembershipResponse> findMembershipByName(String membership) {
        List<Membership> memberships = membershipRepository.findMembershipByName(membership);
        return memberships.stream().map(MembershipMapper::toResponse).toList();
    }

    @Override
    public List<MembershipResponse> findMembershipByGymId(Long gymId) {
        List<Membership> memberships = membershipRepository.findMembershipByGymId(gymId);
        return memberships.stream().map(MembershipMapper::toResponse).toList();
    }
}