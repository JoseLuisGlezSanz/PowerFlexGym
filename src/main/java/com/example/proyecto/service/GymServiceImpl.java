package com.example.proyecto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.proyecto.dto.GymRequest;
import com.example.proyecto.dto.GymResponse;
import com.example.proyecto.mapper.GymMapper;
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
                .map(GymMapper::toResponse)
                .toList();
    }

    @Override
    public GymResponse findById(Integer id) {
        Gym gym = gymRepository.findById(id).orElseThrow(() -> new RuntimeException("Gimnasio no encontrado con ID: " + id));
        return GymMapper.toResponse(gym);
    }

    @Override
    public GymResponse save(GymRequest req) {
        Gym gym = GymMapper.toEntity(req);
        Gym savedGym = gymRepository.save(gym);
        return GymMapper.toResponse(savedGym);
    }

    @Override
    public GymResponse update(Integer id, GymRequest req) {
        Gym existingGym = gymRepository.findById(id).orElseThrow(() -> new RuntimeException("Gimnasio no encontrado con ID: " + id));
        
        GymMapper.copyToEntity(req, existingGym);
        Gym updatedGym = gymRepository.save(existingGym);
        return GymMapper.toResponse(updatedGym);
    }

    @Override
    public void delete(Integer id) {
        gymRepository.deleteById(id);
    }

    @Override
    public List<GymResponse> findByGymName(String gymName) {
        return gymRepository.findByGym(gymName).stream()
                .map(GymMapper::toResponse)
                .toList();
    }
}