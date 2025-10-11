package com.example.proyecto.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.proyecto.dto.MembershipRequest;
import com.example.proyecto.dto.MembershipResponse;
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
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MembershipResponse> findById(Integer id) {
        return membershipRepository.findById(id)
                .map(this::mapToResponse);
    }

    @Override
    public List<MembershipResponse> findByGymId(Integer gymId) {
        return membershipRepository.findByGymIdGym(gymId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MembershipResponse> findByStatus(Integer status) {
        return membershipRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MembershipResponse> findActiveMemberships() {
        return membershipRepository.findByStatus(1).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MembershipResponse> findByNameAndGym(String name, Integer gymId) {
        return membershipRepository.findByMembershipAndGymIdGym(name, gymId)
                .map(this::mapToResponse);
    }

    @Override
    public MembershipResponse create(MembershipRequest request) {
        Gym gym = gymRepository.findById(request.getIdGym())
                .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado"));

        if (membershipRepository.findByMembershipAndGymIdGym(request.getMembership(), request.getIdGym()).isPresent()) {
            throw new RuntimeException("Ya existe una membresía con ese nombre en este gimnasio");
        }

        Membership membership = Membership.builder()
            .membership(request.getMembership())
            .duration(request.getDuration())
            .price(request.getPrice())
            .status(request.getStatus())
            .gym(gym)
            .build();

        Membership saved = membershipRepository.save(membership);
        return mapToResponse(saved);
    }

    @Override
    public MembershipResponse update(Integer id, MembershipRequest request) {
        Membership existing = membershipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membresía no encontrada"));

        Gym gym = gymRepository.findById(request.getIdGym())
                .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado"));

        membershipRepository.findByMembershipAndGymIdGym(request.getMembership(), request.getIdGym())
                .ifPresent(m -> {
                    if (!m.getIdMembership().equals(id)) {
                        throw new RuntimeException("Ya existe otra membresía con ese nombre en este gimnasio");
                    }
                });

        existing.setMembership(request.getMembership());
        existing.setDuration(request.getDuration());
        existing.setPrice(request.getPrice());
        existing.setStatus(request.getStatus());
        existing.setGym(gym);

        Membership updated = membershipRepository.save(existing);
        return mapToResponse(updated);
    }

    @Override
    public void delete(Integer id) {
        if (!membershipRepository.existsById(id)) {
            throw new RuntimeException("Membresía no encontrada");
        }
        membershipRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return membershipRepository.existsById(id);
    }

    private MembershipResponse mapToResponse(Membership membership) {
        return MembershipResponse.builder()
                .idMembership(membership.getIdMembership())
                .membership(membership.getMembership())
                .duration(membership.getDuration())
                .price(membership.getPrice())
                .status(membership.getStatus())
                .idGym(membership.getGym().getIdGym())
                .build();
    }
}