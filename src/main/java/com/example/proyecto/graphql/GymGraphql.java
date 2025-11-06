package com.example.proyecto.graphql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.example.proyecto.dto.GymRequest;
import com.example.proyecto.dto.GymResponse;
import com.example.proyecto.service.GymService;

@Controller
public class GymGraphql {
    @Autowired
    private GymService gymService;

    @QueryMapping
    public List<GymResponse> findAllGyms() {
        return gymService.findAll();
    }

    @QueryMapping
    public GymResponse findByIdGym(@Argument Long id) {
        return gymService.findById(id);
    }

    @MutationMapping
    public GymResponse createGym(@Argument GymRequest gymRequest) {
        return gymService.create(gymRequest);
    }

    @MutationMapping
    public GymResponse updateGym(@Argument Long id, @Argument GymRequest gymRequest) {
        return gymService.update(id, gymRequest);
    }

    // @MutationMapping
    // public Boolean deleteGym(@Argument Long id) {
    //         gymService.delete(id);
    //         return true;
    // }

    @QueryMapping
    public List<GymResponse> findAllGymsPaginated(@Argument int page, @Argument int pageSize) {
        List<GymResponse> gyms = gymService.getAll(page, pageSize);
        return gyms;
    }

    @QueryMapping
    public GymResponse findByNameGym(@Argument String name) {
        return gymService.findByName(name);
    }
}