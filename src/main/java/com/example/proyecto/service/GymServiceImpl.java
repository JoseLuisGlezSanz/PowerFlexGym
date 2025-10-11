package com.example.proyecto.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.proyecto.dto.GymRequest;
import com.example.proyecto.dto.GymResponse;
import com.example.proyecto.model.Gym;
import com.example.proyecto.repository.GymRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GymServiceImpl implements GymService{
    private final GymRepository gymRepository;

    @Override
    public List<GymResponse> findAll() {
        return gymRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<GymResponse> findById(Integer id) {
        return gymRepository.findById(id)
                .map(this::mapToResponse);
    }

    @Override
    public GymResponse create(GymRequest request) {
        Gym gym = Gym.builder()
                .gym(request.getGym())
                .build();

        Gym saved = gymRepository.save(gym);
        return mapToResponse(saved);
    }

    @Override
    public GymResponse update(Integer id, GymRequest request) {
        Gym existing = gymRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado"));

        existing.setGym(request.getGym());

        Gym updated = gymRepository.save(existing);
        return mapToResponse(updated);
    }

    @Override
    public void delete(Integer id) {
        if (!gymRepository.existsById(id)) {
            throw new RuntimeException("Gimnasio no encontrado");
        }
        gymRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return gymRepository.existsById(id);
    }

    private GymResponse mapToResponse(Gym gym) {
        return GymResponse.builder()
                .idGym(gym.getIdGym())
                .gym(gym.getGym())
                .build();
    }
}