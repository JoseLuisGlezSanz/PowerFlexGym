package com.example.proyecto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.proyecto.dto.VisitRequest;
import com.example.proyecto.dto.VisitResponse;
import com.example.proyecto.service.VisitService;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = VisitController.class)
class VisitControllerTest {

    @Autowired
    MockMvc mvc;
    
    @Autowired
    ObjectMapper mapper;

    @Autowired
    VisitService service;

    private static final String BASE = "/api/v1/visits";

    @BeforeEach
    void beforeEach() {
        reset(service);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        VisitService visitService() {
            return mock(VisitService.class);
        }
    }

    // Helpers para crear DTOs de prueba
    private VisitResponse createVisitResponse(Long id, Long customerId, String customerName, 
                                            LocalDateTime date, Boolean pending, String gymName) {
        return VisitResponse.builder()
                .id(id)
                .customerId(customerId)
                .customerName(customerName)
                .date(date)
                .pending(pending)
                .gymId(1L)
                .gymName(gymName)
                .build();
    }

    private VisitRequest createVisitRequest(Long customerId, Boolean pending, Long gymId) {
        VisitRequest request = new VisitRequest();
        request.setCustomerId(customerId);
        request.setPending(pending);
        request.setGymId(gymId);
        return request;
    }

    /*
     * PRUEBA 1: GET /api/v1/visits - Obtener todas las visitas
     */
    @Test
    @DisplayName("GET /api/v1/visits → 200 con lista de visitas")
    void findAll_Ok() throws Exception {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        List<VisitResponse> mockVisits = List.of(
            createVisitResponse(1L, 1L, "Juan Pérez", now.minusHours(2), false, "Gym Central"),
            createVisitResponse(2L, 2L, "María García", now.minusHours(1), true, "Gym Central"),
            createVisitResponse(3L, 1L, "Juan Pérez", now, false, "Gym Central")
        );
        when(service.findAll()).thenReturn(mockVisits);

        // Act & Assert
        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].['Visit identifier']").value(1))
                .andExpect(jsonPath("$[0].customerId").value(1))
                .andExpect(jsonPath("$[0].customerName").value("Juan Pérez"))
                .andExpect(jsonPath("$[0].pending").value(false))
                .andExpect(jsonPath("$[0].gymName").value("Gym Central"))
                .andExpect(jsonPath("$[1].['Visit identifier']").value(2))
                .andExpect(jsonPath("$[1].customerId").value(2))
                .andExpect(jsonPath("$[1].customerName").value("María García"))
                .andExpect(jsonPath("$[1].pending").value(true))
                .andExpect(jsonPath("$[2].['Visit identifier']").value(3))
                .andExpect(jsonPath("$[2].customerId").value(1))
                .andExpect(jsonPath("$[2].customerName").value("Juan Pérez"))
                .andExpect(jsonPath("$[2].pending").value(false));
    }

    @Test
    @DisplayName("GET /api/v1/visits → 200 con lista vacía")
    void findAll_Empty() throws Exception {
        // Arrange
        when(service.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
     * PRUEBA 2: GET /api/v1/visits/{id} - Obtener visita por ID
     */
    @Test
    @DisplayName("GET /api/v1/visits/{id} existente → 200")
    void findById_Ok() throws Exception {
        // Arrange
        LocalDateTime visitDate = LocalDateTime.now().minusHours(1);
        VisitResponse mockVisit = createVisitResponse(1L, 1L, "Juan Pérez", visitDate, false, "Gym Central");
        when(service.findById(1L)).thenReturn(mockVisit);

        // Act & Assert
        mvc.perform(get(BASE + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Visit identifier']").value(1))
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.customerName").value("Juan Pérez"))
                .andExpect(jsonPath("$.pending").value(false))
                .andExpect(jsonPath("$.gymName").value("Gym Central"));
    }

    @Test
    @DisplayName("GET /api/v1/visits/{id} no existente → 404")
    void findById_NotFound() throws Exception {
        // Arrange
        when(service.findById(999L)).thenThrow(new EntityNotFoundException("Visit not found"));

        // Act & Assert
        mvc.perform(get(BASE + "/{id}", 999))
                .andExpect(status().isNotFound());
    }

    /*
     * PRUEBA 3: POST /api/v1/visits - Crear nueva visita
     */
    @Test
    @DisplayName("POST /api/v1/visits → 201 creado exitosamente")
    void create_Ok() throws Exception {
        // Arrange
        VisitRequest request = createVisitRequest(1L, false, 1L);
        VisitResponse response = createVisitResponse(123L, 1L, "Juan Pérez", LocalDateTime.now(), false, "Gym Central");
        
        when(service.create(any(VisitRequest.class))).thenReturn(response);

        // Act & Assert
        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "/api/v1/visits/123"))
                .andExpect(jsonPath("$.['Visit identifier']").value(123))
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.customerName").value("Juan Pérez"))
                .andExpect(jsonPath("$.pending").value(false))
                .andExpect(jsonPath("$.gymName").value("Gym Central"));
    }

    @Test
    @DisplayName("POST /api/v1/visits con datos inválidos → 400")
    void create_InvalidBody() throws Exception {
        // Arrange - Body sin campos requeridos
        String invalidJson = "{}";

        // Act & Assert
        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    /*
     * PRUEBA 4: GET /api/v1/visits/customer/{customerId} - Búsqueda por ID de cliente
     */
    @Test
    @DisplayName("GET /api/v1/visits/customer/{customerId} → 200 con visitas del cliente")
    void findByCustomerId_Ok() throws Exception {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        List<VisitResponse> mockVisits = List.of(
            createVisitResponse(1L, 1L, "Juan Pérez", now.minusDays(1), false, "Gym Central"),
            createVisitResponse(3L, 1L, "Juan Pérez", now, true, "Gym Central")
        );
        when(service.findByCustomerId(1L)).thenReturn(mockVisits);

        // Act & Assert
        mvc.perform(get(BASE + "/customer/{customerId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].['Visit identifier']").value(1))
                .andExpect(jsonPath("$[0].customerId").value(1))
                .andExpect(jsonPath("$[0].customerName").value("Juan Pérez"))
                .andExpect(jsonPath("$[1].['Visit identifier']").value(3))
                .andExpect(jsonPath("$[1].customerId").value(1))
                .andExpect(jsonPath("$[1].customerName").value("Juan Pérez"));
    }

    @Test
    @DisplayName("GET /api/v1/visits/customer/{customerId} → 200 con lista vacía")
    void findByCustomerId_Empty() throws Exception {
        // Arrange
        when(service.findByCustomerId(999L)).thenReturn(Collections.emptyList());

        // Act & Assert
        mvc.perform(get(BASE + "/customer/{customerId}", 999))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
     * PRUEBA 5: GET /api/v1/visits/gym/{gymId} - Búsqueda por ID de gym
     */
    @Test
    @DisplayName("GET /api/v1/visits/gym/{gymId} → 200 con visitas del gym")
    void findByGymId_Ok() throws Exception {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        List<VisitResponse> mockVisits = List.of(
            createVisitResponse(1L, 1L, "Juan Pérez", now.minusDays(1), false, "Gym Central"),
            createVisitResponse(2L, 2L, "María García", now, true, "Gym Central")
        );
        when(service.findByGymId(1L)).thenReturn(mockVisits);

        // Act & Assert
        mvc.perform(get(BASE + "/gym/{gymId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].gymId").value(1))
                .andExpect(jsonPath("$[0].gymName").value("Gym Central"))
                .andExpect(jsonPath("$[1].gymId").value(1))
                .andExpect(jsonPath("$[1].gymName").value("Gym Central"));
    }

    /*
     * PRUEBA 6: PUT /api/v1/visits/{id} - Actualizar visita
     */
    @Test
    @DisplayName("PUT /api/v1/visits/{id} → 204 actualizado exitosamente")
    void update_Ok() throws Exception {
        // Arrange
        VisitRequest request = createVisitRequest(1L, true, 1L);
        VisitResponse response = createVisitResponse(1L, 1L, "Juan Pérez", LocalDateTime.now(), true, "Gym Central");
        
        when(service.update(eq(1L), any(VisitRequest.class))).thenReturn(response);

        // Act & Assert
        mvc.perform(put(BASE + "/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.['Visit identifier']").value(1))
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.customerName").value("Juan Pérez"))
                .andExpect(jsonPath("$.pending").value(true));
    }

    @Test
    @DisplayName("PUT /api/v1/visits/{id} no existente → 404")
    void update_NotFound() throws Exception {
        // Arrange
        VisitRequest request = createVisitRequest(1L, true, 1L);
        when(service.update(eq(999L), any(VisitRequest.class)))
            .thenThrow(new EntityNotFoundException("Visit not found"));

        // Act & Assert
        mvc.perform(put(BASE + "/{id}", 999)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}