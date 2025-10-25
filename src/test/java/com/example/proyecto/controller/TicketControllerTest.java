package com.example.proyecto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.proyecto.dto.TicketRequest;
import com.example.proyecto.dto.TicketResponse;
import com.example.proyecto.service.TicketService;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TicketController.class)
class TicketControllerTest {

    @Autowired
    MockMvc mvc;
    
    @Autowired
    ObjectMapper mapper;

    @Autowired
    TicketService service;

    private static final String BASE = "/api/v1/tickets";

    @BeforeEach
    void beforeEach() {
        reset(service);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        TicketService ticketService() {
            return mock(TicketService.class);
        }
    }

    // Helpers para crear DTOs de prueba
    private TicketResponse createTicketResponse(Integer id, BigDecimal total, String customerName, 
                                               String userName, Integer status) {
        return TicketResponse.builder()
                .idTicket(id)
                .date(LocalDateTime.now())
                .total(total)
                .status(status)
                .saleDate(LocalDateTime.now())
                .methodPayment(1)
                .paymentWith(new BigDecimal("1000.00"))
                .idCustomer(1)
                .customerName(customerName)
                .idUser(1)
                .userName(userName)
                .build();
    }

    private TicketRequest createTicketRequest(BigDecimal total, Integer idCustomer, Integer idUser) {
        TicketRequest request = new TicketRequest();
        request.setDate(LocalDateTime.now());
        request.setTotal(total);
        request.setStatus(1);
        request.setSaleDate(LocalDateTime.now());
        request.setMethodPayment(1);
        request.setPaymentWith(new BigDecimal("1000.00"));
        request.setIdCustomer(idCustomer);
        request.setIdUser(idUser);
        return request;
    }

    /*
     * PRUEBA 1: GET /api/v1/tickets - Obtener todos los tickets
     */
    @Test
    @DisplayName("GET /api/v1/tickets → 200 con lista de tickets")
    void findAll_Ok() throws Exception {
        // Arrange
        List<TicketResponse> mockTickets = List.of(
            createTicketResponse(1, new BigDecimal("1500.00"), "Juan Pérez", "Admin User", 1),
            createTicketResponse(2, new BigDecimal("500.50"), "María García", "Vendedor User", 1),
            createTicketResponse(3, new BigDecimal("750.25"), "Carlos López", "Admin User", 0)
        );
        when(service.findAll()).thenReturn(mockTickets);

        // Act & Assert
        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].['Ticket identifier']").value(1))
                .andExpect(jsonPath("$[0].total").value(1500.00))
                .andExpect(jsonPath("$[0].customerName").value("Juan Pérez"))
                .andExpect(jsonPath("$[0].userName").value("Admin User"))
                .andExpect(jsonPath("$[0].status").value(1))
                .andExpect(jsonPath("$[0].methodPayment").value(1))
                .andExpect(jsonPath("$[0].paymentWith").value(1000.00))
                .andExpect(jsonPath("$[1].['Ticket identifier']").value(2))
                .andExpect(jsonPath("$[1].total").value(500.50))
                .andExpect(jsonPath("$[1].customerName").value("María García"))
                .andExpect(jsonPath("$[2].['Ticket identifier']").value(3))
                .andExpect(jsonPath("$[2].total").value(750.25))
                .andExpect(jsonPath("$[2].status").value(0));
    }

    @Test
    @DisplayName("GET /api/v1/tickets → 200 con lista vacía")
    void findAll_Empty() throws Exception {
        // Arrange
        when(service.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
     * PRUEBA 2: GET /api/v1/tickets/{id} - Obtener ticket por ID
     */
    @Test
    @DisplayName("GET /api/v1/tickets/{id} existente → 200")
    void findById_Ok() throws Exception {
        // Arrange
        TicketResponse mockTicket = createTicketResponse(1, new BigDecimal("1500.00"), "Juan Pérez", "Admin User", 1);
        when(service.findById(1)).thenReturn(mockTicket);

        // Act & Assert
        mvc.perform(get(BASE + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Ticket identifier']").value(1))
                .andExpect(jsonPath("$.total").value(1500.00))
                .andExpect(jsonPath("$.customerName").value("Juan Pérez"))
                .andExpect(jsonPath("$.userName").value("Admin User"))
                .andExpect(jsonPath("$.status").value(1))
                .andExpect(jsonPath("$.methodPayment").value(1))
                .andExpect(jsonPath("$.paymentWith").value(1000.00));
    }

    @Test
    @DisplayName("GET /api/v1/tickets/{id} no existente → 404")
    void findById_NotFound() throws Exception {
        // Arrange
        when(service.findById(999)).thenThrow(new EntityNotFoundException("Ticket not found"));

        // Act & Assert
        mvc.perform(get(BASE + "/{id}", 999))
                .andExpect(status().isNotFound());
    }

    /*
     * PRUEBA 3: POST /api/v1/tickets - Crear nuevo ticket
     * Transacción financiera crítica
     */
    @Test
    @DisplayName("POST /api/v1/tickets → 201 creado exitosamente")
    void create_Ok() throws Exception {
        // Arrange
        TicketRequest request = createTicketRequest(new BigDecimal("1200.75"), 1, 1);
        TicketResponse response = createTicketResponse(123, new BigDecimal("1200.75"), "Juan Pérez", "Admin User", 1);
        
        when(service.save(any(TicketRequest.class))).thenReturn(response);

        // Act & Assert
        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.['Ticket identifier']").value(123))
                .andExpect(jsonPath("$.total").value(1200.75))
                .andExpect(jsonPath("$.customerName").value("Juan Pérez"))
                .andExpect(jsonPath("$.userName").value("Admin User"))
                .andExpect(jsonPath("$.status").value(1))
                .andExpect(jsonPath("$.methodPayment").value(1));
    }

    @Test
    @DisplayName("POST /api/v1/tickets con datos inválidos → 400")
    void create_InvalidBody() throws Exception {
        // Arrange - Body sin campos requeridos
        TicketRequest invalidRequest = new TicketRequest(); // Todos los campos null

        // Act & Assert
        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    /*
     * PRUEBA 4: GET /api/v1/tickets/customer/{idCustomer} - Búsqueda por ID de cliente
     * Endpoint crítico para historial de compras
     */
    @Test
    @DisplayName("GET /api/v1/tickets/customer/{idCustomer} → 200 con tickets del cliente")
    void findByCustomerId_Ok() throws Exception {
        // Arrange
        List<TicketResponse> mockTickets = List.of(
            createTicketResponse(1, new BigDecimal("1500.00"), "Juan Pérez", "Admin User", 1),
            createTicketResponse(2, new BigDecimal("500.50"), "Juan Pérez", "Vendedor User", 1),
            createTicketResponse(3, new BigDecimal("750.25"), "Juan Pérez", "Admin User", 0)
        );
        when(service.findByCustomerId(1)).thenReturn(mockTickets);

        // Act & Assert
        mvc.perform(get(BASE + "/customer/{idCustomer}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].['Ticket identifier']").value(1))
                .andExpect(jsonPath("$[0].customerName").value("Juan Pérez"))
                .andExpect(jsonPath("$[0].total").value(1500.00))
                .andExpect(jsonPath("$[1].['Ticket identifier']").value(2))
                .andExpect(jsonPath("$[1].customerName").value("Juan Pérez"))
                .andExpect(jsonPath("$[1].total").value(500.50))
                .andExpect(jsonPath("$[2].['Ticket identifier']").value(3))
                .andExpect(jsonPath("$[2].customerName").value("Juan Pérez"))
                .andExpect(jsonPath("$[2].total").value(750.25));
    }

    @Test
    @DisplayName("GET /api/v1/tickets/customer/{idCustomer} → 200 con lista vacía")
    void findByCustomerId_Empty() throws Exception {
        // Arrange
        when(service.findByCustomerId(999)).thenReturn(Collections.emptyList());

        // Act & Assert
        mvc.perform(get(BASE + "/customer/{idCustomer}", 999))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}