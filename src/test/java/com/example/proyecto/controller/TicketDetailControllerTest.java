package com.example.proyecto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.proyecto.dto.TicketDetailRequest;
import com.example.proyecto.dto.TicketDetailResponse;
import com.example.proyecto.service.TicketDetailService;
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
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TicketDetailController.class)
class TicketDetailControllerTest {

    @Autowired
    MockMvc mvc;
    
    @Autowired
    ObjectMapper mapper;

    @Autowired
    TicketDetailService service;

    private static final String BASE = "/api/v1/ticket-details";

    @BeforeEach
    void beforeEach() {
        reset(service);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        TicketDetailService ticketDetailService() {
            return mock(TicketDetailService.class);
        }
    }

    // Helpers para crear DTOs de prueba
    private TicketDetailResponse createTicketDetailResponse(Integer id, Integer amount, Double unitPrice, 
                                                           BigDecimal subtotal, String productName, Integer idTicket) {
        return TicketDetailResponse.builder()
                .idDetailTicket(id)
                .amount(amount)
                .unitPrice(unitPrice)
                .subtotal(subtotal)
                .idProduct(1)
                .productName(productName)
                .idTicket(idTicket)
                .build();
    }

    private TicketDetailRequest createTicketDetailRequest(Integer amount, Double unitPrice, BigDecimal subtotal, 
                                                         Integer idProduct, Integer idTicket) {
        TicketDetailRequest request = new TicketDetailRequest();
        request.setAmount(amount);
        request.setUnitPrice(unitPrice);
        request.setSubtotal(subtotal);
        request.setIdProduct(idProduct);
        request.setIdTicket(idTicket);
        return request;
    }

    /*
     * PRUEBA 1: GET /api/v1/ticket-details - Obtener todos los detalles de ticket
     */
    @Test
    @DisplayName("GET /api/v1/ticket-details → 200 con lista de detalles")
    void findAll_Ok() throws Exception {
        // Arrange
        List<TicketDetailResponse> mockDetails = List.of(
            createTicketDetailResponse(1, 2, 899.99, new BigDecimal("1799.98"), "Proteína Whey", 1),
            createTicketDetailResponse(2, 1, 499.50, new BigDecimal("499.50"), "Creatina 300g", 1),
            createTicketDetailResponse(3, 3, 199.99, new BigDecimal("599.97"), "Shaker", 2)
        );
        when(service.findAll()).thenReturn(mockDetails);

        // Act & Assert
        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].['Detail identifier']").value(1))
                .andExpect(jsonPath("$[0].amount").value(2))
                .andExpect(jsonPath("$[0].unitPrice").value(899.99))
                .andExpect(jsonPath("$[0].subtotal").value(1799.98))
                .andExpect(jsonPath("$[0].productName").value("Proteína Whey"))
                .andExpect(jsonPath("$[0].idTicket").value(1))
                .andExpect(jsonPath("$[1].['Detail identifier']").value(2))
                .andExpect(jsonPath("$[1].amount").value(1))
                .andExpect(jsonPath("$[1].unitPrice").value(499.50))
                .andExpect(jsonPath("$[1].subtotal").value(499.50))
                .andExpect(jsonPath("$[1].productName").value("Creatina 300g"))
                .andExpect(jsonPath("$[2].['Detail identifier']").value(3))
                .andExpect(jsonPath("$[2].amount").value(3))
                .andExpect(jsonPath("$[2].unitPrice").value(199.99))
                .andExpect(jsonPath("$[2].subtotal").value(599.97))
                .andExpect(jsonPath("$[2].productName").value("Shaker"));
    }

    @Test
    @DisplayName("GET /api/v1/ticket-details → 200 con lista vacía")
    void findAll_Empty() throws Exception {
        // Arrange
        when(service.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
     * PRUEBA 2: GET /api/v1/ticket-details/{id} - Obtener detalle por ID
     */
    @Test
    @DisplayName("GET /api/v1/ticket-details/{id} existente → 200")
    void findById_Ok() throws Exception {
        // Arrange
        TicketDetailResponse mockDetail = createTicketDetailResponse(1, 2, 899.99, new BigDecimal("1799.98"), "Proteína Whey", 1);
        when(service.findById(1)).thenReturn(mockDetail);

        // Act & Assert
        mvc.perform(get(BASE + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Detail identifier']").value(1))
                .andExpect(jsonPath("$.amount").value(2))
                .andExpect(jsonPath("$.unitPrice").value(899.99))
                .andExpect(jsonPath("$.subtotal").value(1799.98))
                .andExpect(jsonPath("$.productName").value("Proteína Whey"))
                .andExpect(jsonPath("$.idTicket").value(1));
    }

    @Test
    @DisplayName("GET /api/v1/ticket-details/{id} no existente → 404")
    void findById_NotFound() throws Exception {
        // Arrange
        when(service.findById(999)).thenThrow(new EntityNotFoundException("Ticket detail not found"));

        // Act & Assert
        mvc.perform(get(BASE + "/{id}", 999))
                .andExpect(status().isNotFound());
    }

    /*
     * PRUEBA 3: POST /api/v1/ticket-details - Crear nuevo detalle de ticket
     * Crítico para el cálculo de subtotales en ventas
     */
    @Test
    @DisplayName("POST /api/v1/ticket-details → 201 creado exitosamente")
    void create_Ok() throws Exception {
        // Arrange
        TicketDetailRequest request = createTicketDetailRequest(2, 899.99, new BigDecimal("1799.98"), 1, 1);
        TicketDetailResponse response = createTicketDetailResponse(123, 2, 899.99, new BigDecimal("1799.98"), "Proteína Whey", 1);
        
        when(service.save(any(TicketDetailRequest.class))).thenReturn(response);

        // Act & Assert
        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.['Detail identifier']").value(123))
                .andExpect(jsonPath("$.amount").value(2))
                .andExpect(jsonPath("$.unitPrice").value(899.99))
                .andExpect(jsonPath("$.subtotal").value(1799.98))
                .andExpect(jsonPath("$.productName").value("Proteína Whey"))
                .andExpect(jsonPath("$.idTicket").value(1));
    }

    @Test
    @DisplayName("POST /api/v1/ticket-details con datos inválidos → 400")
    void create_InvalidBody() throws Exception {
        // Arrange - Body sin campos requeridos
        TicketDetailRequest invalidRequest = new TicketDetailRequest(); // Todos los campos null

        // Act & Assert
        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    /*
     * PRUEBA 4: GET /api/v1/ticket-details/ticket/{idTicket} - Búsqueda por ID de ticket
     * Endpoint crítico para obtener el detalle completo de una venta
     */
    @Test
    @DisplayName("GET /api/v1/ticket-details/ticket/{idTicket} → 200 con detalles del ticket")
    void findByTicketId_Ok() throws Exception {
        // Arrange
        List<TicketDetailResponse> mockDetails = List.of(
            createTicketDetailResponse(1, 2, 899.99, new BigDecimal("1799.98"), "Proteína Whey", 1),
            createTicketDetailResponse(2, 1, 499.50, new BigDecimal("499.50"), "Creatina 300g", 1),
            createTicketDetailResponse(3, 1, 199.99, new BigDecimal("199.99"), "Shaker", 1)
        );
        when(service.findByTicketId(1)).thenReturn(mockDetails);

        // Act & Assert
        mvc.perform(get(BASE + "/ticket/{idTicket}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].['Detail identifier']").value(1))
                .andExpect(jsonPath("$[0].idTicket").value(1))
                .andExpect(jsonPath("$[0].productName").value("Proteína Whey"))
                .andExpect(jsonPath("$[0].subtotal").value(1799.98))
                .andExpect(jsonPath("$[1].['Detail identifier']").value(2))
                .andExpect(jsonPath("$[1].idTicket").value(1))
                .andExpect(jsonPath("$[1].productName").value("Creatina 300g"))
                .andExpect(jsonPath("$[1].subtotal").value(499.50))
                .andExpect(jsonPath("$[2].['Detail identifier']").value(3))
                .andExpect(jsonPath("$[2].idTicket").value(1))
                .andExpect(jsonPath("$[2].productName").value("Shaker"))
                .andExpect(jsonPath("$[2].subtotal").value(199.99));
    }

    @Test
    @DisplayName("GET /api/v1/ticket-details/ticket/{idTicket} → 200 con lista vacía")
    void findByTicketId_Empty() throws Exception {
        // Arrange
        when(service.findByTicketId(999)).thenReturn(Collections.emptyList());

        // Act & Assert
        mvc.perform(get(BASE + "/ticket/{idTicket}", 999))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}