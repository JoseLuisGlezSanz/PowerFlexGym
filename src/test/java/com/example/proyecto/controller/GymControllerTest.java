package com.example.proyecto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.proyecto.dto.GymRequest;
import com.example.proyecto.dto.GymResponse;
import com.example.proyecto.service.GymService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GymController.class)
class GymControllerTest {

    @Autowired
    MockMvc mvc;
    
    @Autowired
    ObjectMapper mapper;

    @Autowired
    GymService service;

    private static final String BASE = "/api/v1/gyms";

    @BeforeEach
    void beforeEach() {
        reset(service);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        GymService gymService() {
            return mock(GymService.class);
        }
    }

    // Helpers para crear DTOs de prueba
    private GymResponse createGymResponse(Integer id, String name) {
        return GymResponse.builder()
                .idGym(id)
                .gym(name)
                .build();
    }

    private GymRequest createGymRequest(String name) {
        GymRequest request = new GymRequest();
        request.setGym(name);
        return request;
    }

    /*
     * PRUEBA 1: GET /api/v1/gyms - Obtener todos los gimnasios
     */
    @Test
    @DisplayName("GET /api/v1/gyms → 200 con lista de gimnasios")
    void findAll_Ok() throws Exception {
        // Arrange
        List<GymResponse> mockGyms = List.of(
            createGymResponse(1, "Gym Central"),
            createGymResponse(2, "Gym Norte"),
            createGymResponse(3, "Gym Sur")
        );
        when(service.findAll()).thenReturn(mockGyms);

        // Act & Assert
        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].['Gym identifier:']").value(1))
                .andExpect(jsonPath("$[0].gym").value("Gym Central"))
                .andExpect(jsonPath("$[1].['Gym identifier:']").value(2))
                .andExpect(jsonPath("$[1].gym").value("Gym Norte"))
                .andExpect(jsonPath("$[2].['Gym identifier:']").value(3))
                .andExpect(jsonPath("$[2].gym").value("Gym Sur"));
    }

    @Test
    @DisplayName("GET /api/v1/gyms → 200 con lista vacía")
    void findAll_Empty() throws Exception {
        // Arrange
        when(service.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
     * PRUEBA 2: GET /api/v1/gyms/{id} - Obtener gimnasio por ID
     */
    @Test
    @DisplayName("GET /api/v1/gyms/{id} existente → 200")
    void findById_Ok() throws Exception {
        // Arrange
        GymResponse mockGym = createGymResponse(1, "Gym Central");
        when(service.findById(1)).thenReturn(mockGym);

        // Act & Assert
        mvc.perform(get(BASE + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Gym identifier:']").value(1))
                .andExpect(jsonPath("$.gym").value("Gym Central"));
    }

    @Test
    @DisplayName("GET /api/v1/gyms/{id} no existente → 404")
    void findById_NotFound() throws Exception {
        // Arrange
        when(service.findById(999)).thenThrow(new EntityNotFoundException("Gym not found"));

        // Act & Assert
        mvc.perform(get(BASE + "/{id}", 999))
                .andExpect(status().isNotFound());
    }

    /*
     * PRUEBA 3: POST /api/v1/gyms - Crear nuevo gimnasio
     */
    @Test
    @DisplayName("POST /api/v1/gyms → 201 creado exitosamente")
    void create_Ok() throws Exception {
        // Arrange
        GymRequest request = createGymRequest("Nuevo Gym");
        GymResponse response = createGymResponse(123, "Nuevo Gym");
        
        when(service.save(any(GymRequest.class))).thenReturn(response);

        // Act & Assert
        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.['Gym identifier:']").value(123))
                .andExpect(jsonPath("$.gym").value("Nuevo Gym"));
    }

    @Test
    @DisplayName("POST /api/v1/gyms con body vacío → 400")
    void create_InvalidBody() throws Exception {
        // Arrange - Body sin campos requeridos
        GymRequest invalidRequest = createGymRequest(null);

        // Act & Assert
        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    /*
     * PRUEBA 4: PUT /api/v1/gyms/{id} - Actualizar gimnasio existente
     */
    @Test
    @DisplayName("PUT /api/v1/gyms/{id} → 200 actualizado exitosamente")
    void update_Ok() throws Exception {
        // Arrange
        GymRequest request = createGymRequest("Gym Central Actualizado");
        GymResponse response = createGymResponse(1, "Gym Central Actualizado");
        
        when(service.update(eq(1), any(GymRequest.class))).thenReturn(response);

        // Act & Assert
        mvc.perform(put(BASE + "/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Gym identifier:']").value(1))
                .andExpect(jsonPath("$.gym").value("Gym Central Actualizado"));
    }

    @Test
    @DisplayName("PUT /api/v1/gyms/{id} no existente → 404")
    void update_NotFound() throws Exception {
        // Arrange
        GymRequest request = createGymRequest("Gym Actualizado");
        when(service.update(eq(999), any(GymRequest.class)))
                .thenThrow(new EntityNotFoundException("Gym not found"));

        // Act & Assert
        mvc.perform(put(BASE + "/{id}", 999)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}