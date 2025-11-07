package com.example.proyecto.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public GymResponse findById(Long id) {
        Gym gym = gymRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado con ID: " + id));
        return GymMapper.toResponse(gym);
    }

    @Override
    public GymResponse create(GymRequest gymRequest) {
        Gym gym = GymMapper.toEntity(gymRequest);
        Gym savedGym = gymRepository.save(gym);
        return GymMapper.toResponse(savedGym);
    }

    @Override
    public GymResponse update(Long id, GymRequest gymRequest) {
        Gym existingGym = gymRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado con ID: " + id));
        
        GymMapper.copyToEntity(gymRequest, existingGym);
        Gym updatedGym = gymRepository.save(existingGym);
        return GymMapper.toResponse(updatedGym);
    }

     // @Override
     // public void delete(Long id){
     //      gymRepository.deleteById(id);
     // }

    @Override
    public List<GymResponse> getAll(int page, int pageSize) {
        PageRequest pageReq = PageRequest.of(page, pageSize);
        Page<Gym> gyms = gymRepository.findAll(pageReq);
        return gyms.getContent().stream().map(GymMapper::toResponse).toList();
    }

    @Override
    public GymResponse findByName(String name) {
        Gym gym = gymRepository.findByName(name);
        return GymMapper.toResponse(gym);
    }
}