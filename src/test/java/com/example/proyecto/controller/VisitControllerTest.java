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
    private VisitResponse createVisitResponse(Integer id, Integer idCustomer, String customerName, 
                                            LocalDateTime date, Integer pending, String gymName) {
        return VisitResponse.builder()
                .idVisit(id)
                .idCustomer(idCustomer)
                .customerName(customerName)
                .date(date)
                .pending(pending)
                .idGym(1)
                .gymName(gymName)
                .build();
    }

    private VisitRequest createVisitRequest(Integer idCustomer, LocalDateTime date, Integer pending, Integer idGym) {
        VisitRequest request = new VisitRequest();
        request.setIdCustomer(idCustomer);
        request.setDate(date);
        request.setPending(pending);
        request.setIdGym(idGym);
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
            createVisitResponse(1, 1, "Juan Pérez", now.minusHours(2), 0, "Gym Central"),
            createVisitResponse(2, 2, "María García", now.minusHours(1), 1, "Gym Central"),
            createVisitResponse(3, 1, "Juan Pérez", now, 0, "Gym Central")
        );
        when(service.findAll()).thenReturn(mockVisits);

        // Act & Assert
        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].['Visit identifier']").value(1))
                .andExpect(jsonPath("$[0].idCustomer").value(1))
                .andExpect(jsonPath("$[0].customerName").value("Juan Pérez"))
                .andExpect(jsonPath("$[0].pending").value(0))
                .andExpect(jsonPath("$[0].gymName").value("Gym Central"))
                .andExpect(jsonPath("$[1].['Visit identifier']").value(2))
                .andExpect(jsonPath("$[1].idCustomer").value(2))
                .andExpect(jsonPath("$[1].customerName").value("María García"))
                .andExpect(jsonPath("$[1].pending").value(1))
                .andExpect(jsonPath("$[2].['Visit identifier']").value(3))
                .andExpect(jsonPath("$[2].idCustomer").value(1))
                .andExpect(jsonPath("$[2].customerName").value("Juan Pérez"))
                .andExpect(jsonPath("$[2].pending").value(0));
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
        VisitResponse mockVisit = createVisitResponse(1, 1, "Juan Pérez", visitDate, 0, "Gym Central");
        when(service.findById(1)).thenReturn(mockVisit);

        // Act & Assert
        mvc.perform(get(BASE + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Visit identifier']").value(1))
                .andExpect(jsonPath("$.idCustomer").value(1))
                .andExpect(jsonPath("$.customerName").value("Juan Pérez"))
                .andExpect(jsonPath("$.pending").value(0))
                .andExpect(jsonPath("$.gymName").value("Gym Central"));
    }

    @Test
    @DisplayName("GET /api/v1/visits/{id} no existente → 404")
    void findById_NotFound() throws Exception {
        // Arrange
        when(service.findById(999)).thenThrow(new EntityNotFoundException("Visit not found"));

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
        LocalDateTime visitDate = LocalDateTime.now();
        VisitRequest request = createVisitRequest(1, visitDate, 0, 1);
        VisitResponse response = createVisitResponse(123, 1, "Juan Pérez", visitDate, 0, "Gym Central");
        
        when(service.save(any(VisitRequest.class))).thenReturn(response);

        // Act & Assert
        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.['Visit identifier']").value(123))
                .andExpect(jsonPath("$.idCustomer").value(1))
                .andExpect(jsonPath("$.customerName").value("Juan Pérez"))
                .andExpect(jsonPath("$.pending").value(0))
                .andExpect(jsonPath("$.gymName").value("Gym Central"));
    }

    @Test
    @DisplayName("POST /api/v1/visits con datos inválidos → 400")
    void create_InvalidBody() throws Exception {
        // Arrange - Body sin campos requeridos
        VisitRequest invalidRequest = new VisitRequest(); // Todos los campos null

        // Act & Assert
        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    /*
     * PRUEBA 4: GET /api/v1/visits/customer/{idCustomer} - Búsqueda por ID de cliente
     */
    @Test
    @DisplayName("GET /api/v1/visits/customer/{idCustomer} → 200 con visitas del cliente")
    void findByCustomerId_Ok() throws Exception {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        List<VisitResponse> mockVisits = List.of(
            createVisitResponse(1, 1, "Juan Pérez", now.minusDays(1), 0, "Gym Central"),
            createVisitResponse(3, 1, "Juan Pérez", now, 0, "Gym Central")
        );
        when(service.findByCustomerId(1)).thenReturn(mockVisits);

        // Act & Assert
        mvc.perform(get(BASE + "/customer/{idCustomer}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].['Visit identifier']").value(1))
                .andExpect(jsonPath("$[0].idCustomer").value(1))
                .andExpect(jsonPath("$[0].customerName").value("Juan Pérez"))
                .andExpect(jsonPath("$[1].['Visit identifier']").value(3))
                .andExpect(jsonPath("$[1].idCustomer").value(1))
                .andExpect(jsonPath("$[1].customerName").value("Juan Pérez"));
    }
}