package com.example.proyecto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.proyecto.dto.MembershipRequest;
import com.example.proyecto.dto.MembershipResponse;
import com.example.proyecto.dto.GymResponse;
import com.example.proyecto.service.MembershipService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MembershipController.class)
class MembershipControllerTest {

    @Autowired
    MockMvc mvc;
    
    @Autowired
    ObjectMapper mapper;

    @Autowired
    MembershipService service;

    private static final String BASE = "/api/v1/memberships";

    @BeforeEach
    void beforeEach() {
        reset(service);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        MembershipService membershipService() {
            return mock(MembershipService.class);
        }
    }

    // Helpers para crear DTOs de prueba
    private MembershipResponse createMembershipResponse(Integer id, String name, String duration, Double price) {
        return MembershipResponse.builder()
                .idMembership(id)
                .membership(name)
                .duration(duration)
                .price(price)
                .status(1)
                .gym(GymResponse.builder()
                        .idGym(1)
                        .gym("Gym Central")
                        .build())
                .build();
    }

    private MembershipRequest createMembershipRequest(String name, String duration, Double price, Integer idGym) {
        MembershipRequest request = new MembershipRequest();
        request.setMembership(name);
        request.setDuration(duration);
        request.setPrice(price);
        request.setStatus(1);
        request.setIdGym(idGym);
        return request;
    }

    /*
     * PRUEBA 1: GET /api/v1/memberships - Obtener todas las membresías
     */
    @Test
    @DisplayName("GET /api/v1/memberships → 200 con lista de membresías")
    void findAll_Ok() throws Exception {
        // Arrange
        List<MembershipResponse> mockMemberships = List.of(
            createMembershipResponse(1, "Membresía Premium", "3 meses", 1500.0),
            createMembershipResponse(2, "Membresía Básica", "1 mes", 500.0)
        );
        when(service.findAll()).thenReturn(mockMemberships);

        // Act & Assert
        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].['Membership identifier:']").value(1))
                .andExpect(jsonPath("$[0].membership").value("Membresía Premium"))
                .andExpect(jsonPath("$[0].duration").value("3 meses"))
                .andExpect(jsonPath("$[0].price").value(1500.0))
                .andExpect(jsonPath("$[0].status").value(1))
                .andExpect(jsonPath("$[1].['Membership identifier:']").value(2))
                .andExpect(jsonPath("$[1].membership").value("Membresía Básica"))
                .andExpect(jsonPath("$[1].price").value(500.0));
    }

    @Test
    @DisplayName("GET /api/v1/memberships → 200 con lista vacía")
    void findAll_Empty() throws Exception {
        // Arrange
        when(service.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
     * PRUEBA 2: GET /api/v1/memberships/{id} - Obtener membresía por ID
     */
    @Test
    @DisplayName("GET /api/v1/memberships/{id} existente → 200")
    void findById_Ok() throws Exception {
        // Arrange
        MembershipResponse mockMembership = createMembershipResponse(1, "Membresía Premium", "3 meses", 1500.0);
        when(service.findById(1)).thenReturn(mockMembership);

        // Act & Assert
        mvc.perform(get(BASE + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Membership identifier:']").value(1))
                .andExpect(jsonPath("$.membership").value("Membresía Premium"))
                .andExpect(jsonPath("$.duration").value("3 meses"))
                .andExpect(jsonPath("$.price").value(1500.0))
                .andExpect(jsonPath("$.status").value(1))
                .andExpect(jsonPath("$.gym.gym").value("Gym Central"));
    }

    @Test
    @DisplayName("GET /api/v1/memberships/{id} no existente → 404")
    void findById_NotFound() throws Exception {
        // Arrange
        when(service.findById(999)).thenThrow(new EntityNotFoundException("Membership not found"));

        // Act & Assert
        mvc.perform(get(BASE + "/{id}", 999))
                .andExpect(status().isNotFound());
    }

    /*
     * PRUEBA 3: POST /api/v1/memberships - Crear nueva membresía
     */
    @Test
    @DisplayName("POST /api/v1/memberships → 201 creado exitosamente")
    void create_Ok() throws Exception {
        // Arrange
        MembershipRequest request = createMembershipRequest("Nueva Membresía", "6 meses", 2500.0, 1);
        MembershipResponse response = createMembershipResponse(123, "Nueva Membresía", "6 meses", 2500.0);
        
        when(service.save(any(MembershipRequest.class))).thenReturn(response);

        // Act & Assert
        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.['Membership identifier:']").value(123))
                .andExpect(jsonPath("$.membership").value("Nueva Membresía"))
                .andExpect(jsonPath("$.duration").value("6 meses"))
                .andExpect(jsonPath("$.price").value(2500.0))
                .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    @DisplayName("POST /api/v1/memberships con datos inválidos → 400")
    void create_InvalidBody() throws Exception {
        // Arrange - Body sin campos requeridos
        MembershipRequest invalidRequest = new MembershipRequest(); // Todos los campos null

        // Act & Assert
        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    /*
     * PRUEBA 4: GET /api/v1/memberships/gym/{idGym} - Búsqueda por ID de gym
     * Endpoint específico de negocio muy importante
     */
    @Test
    @DisplayName("GET /api/v1/memberships/gym/{idGym} → 200 con membresías del gym")
    void findByGymId_Ok() throws Exception {
        // Arrange
        List<MembershipResponse> mockMemberships = List.of(
            createMembershipResponse(1, "Membresía Premium", "3 meses", 1500.0),
            createMembershipResponse(2, "Membresía Básica", "1 mes", 500.0)
        );
        when(service.findByGymId(1)).thenReturn(mockMemberships);

        // Act & Assert
        mvc.perform(get(BASE + "/gym/{idGym}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].['Membership identifier:']").value(1))
                .andExpect(jsonPath("$[0].membership").value("Membresía Premium"))
                .andExpect(jsonPath("$[0].gym.gym").value("Gym Central"))
                .andExpect(jsonPath("$[1].['Membership identifier:']").value(2))
                .andExpect(jsonPath("$[1].membership").value("Membresía Básica"));
    }

    @Test
    @DisplayName("GET /api/v1/memberships/gym/{idGym} → 200 con lista vacía")
    void findByGymId_Empty() throws Exception {
        // Arrange
        when(service.findByGymId(999)).thenReturn(Collections.emptyList());

        // Act & Assert
        mvc.perform(get(BASE + "/gym/{idGym}", 999))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}