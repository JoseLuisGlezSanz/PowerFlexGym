package com.example.proyecto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.proyecto.dto.MembershipSaleRequest;
import com.example.proyecto.dto.MembershipSaleResponse;
import com.example.proyecto.service.MembershipSaleService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MembershipSaleController.class)
class MembershipSaleControllerTest {

    @Autowired
    MockMvc mvc;
    
    @Autowired
    ObjectMapper mapper;

    @Autowired
    MembershipSaleService service;

    private static final String BASE = "/api/v1/membership-sales";

    @BeforeEach
    void beforeEach() {
        reset(service);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        MembershipSaleService membershipSaleService() {
            return mock(MembershipSaleService.class);
        }
    }

    // Helpers para crear DTOs de prueba
    private MembershipSaleResponse createMembershipSaleResponse(Integer id, Double payment, 
                                                               String customerName, String membershipName, 
                                                               String gymName, String userName) {
        return MembershipSaleResponse.builder()
                .idMembershipSale(id)
                .date(LocalDateTime.now())
                .payment(payment)
                .cancellation(false)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(1))
                .idMembership(1)
                .membershipName(membershipName)
                .idCustomer(1)
                .customerName(customerName)
                .idGym(1)
                .gymName(gymName)
                .idUser(1)
                .userName(userName)
                .build();
    }

    private MembershipSaleRequest createMembershipSaleRequest(Double payment, Integer idMembership, 
                                                             Integer idCustomer, Integer idGym, Integer idUser) {
        MembershipSaleRequest request = new MembershipSaleRequest();
        request.setDate(LocalDateTime.now());
        request.setPayment(payment);
        request.setStartDate(LocalDate.now());
        request.setEndDate(LocalDate.now().plusMonths(1));
        request.setIdMembership(idMembership);
        request.setIdCustomer(idCustomer);
        request.setIdGym(idGym);
        request.setIdUser(idUser);
        request.setCancellation(false);
        return request;
    }

    /*
     * PRUEBA 1: GET /api/v1/membership-sales - Obtener todas las ventas de membresías
     */
    @Test
    @DisplayName("GET /api/v1/membership-sales → 200 con lista de ventas")
    void findAll_Ok() throws Exception {
        // Arrange
        List<MembershipSaleResponse> mockSales = List.of(
            createMembershipSaleResponse(1, 1500.0, "Juan Pérez", "Membresía Premium", "Gym Central", "Admin User"),
            createMembershipSaleResponse(2, 500.0, "María García", "Membresía Básica", "Gym Norte", "Vendedor User")
        );
        when(service.findAll()).thenReturn(mockSales);

        // Act & Assert
        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].['Sale identifier']").value(1))
                .andExpect(jsonPath("$[0].payment").value(1500.0))
                .andExpect(jsonPath("$[0].customerName").value("Juan Pérez"))
                .andExpect(jsonPath("$[0].membershipName").value("Membresía Premium"))
                .andExpect(jsonPath("$[0].gymName").value("Gym Central"))
                .andExpect(jsonPath("$[0].userName").value("Admin User"))
                .andExpect(jsonPath("$[0].cancellation").value(false))
                .andExpect(jsonPath("$[1].['Sale identifier']").value(2))
                .andExpect(jsonPath("$[1].payment").value(500.0))
                .andExpect(jsonPath("$[1].customerName").value("María García"));
    }

    @Test
    @DisplayName("GET /api/v1/membership-sales → 200 con lista vacía")
    void findAll_Empty() throws Exception {
        // Arrange
        when(service.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
     * PRUEBA 2: GET /api/v1/membership-sales/{id} - Obtener venta por ID
     */
    @Test
    @DisplayName("GET /api/v1/membership-sales/{id} existente → 200")
    void findById_Ok() throws Exception {
        // Arrange
        MembershipSaleResponse mockSale = createMembershipSaleResponse(1, 1500.0, "Juan Pérez", "Membresía Premium", "Gym Central", "Admin User");
        when(service.findById(1)).thenReturn(mockSale);

        // Act & Assert
        mvc.perform(get(BASE + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Sale identifier']").value(1))
                .andExpect(jsonPath("$.payment").value(1500.0))
                .andExpect(jsonPath("$.customerName").value("Juan Pérez"))
                .andExpect(jsonPath("$.membershipName").value("Membresía Premium"))
                .andExpect(jsonPath("$.gymName").value("Gym Central"))
                .andExpect(jsonPath("$.userName").value("Admin User"))
                .andExpect(jsonPath("$.cancellation").value(false));
    }

    @Test
    @DisplayName("GET /api/v1/membership-sales/{id} no existente → 404")
    void findById_NotFound() throws Exception {
        // Arrange
        when(service.findById(999)).thenThrow(new EntityNotFoundException("Membership sale not found"));

        // Act & Assert
        mvc.perform(get(BASE + "/{id}", 999))
                .andExpect(status().isNotFound());
    }

    /*
     * PRUEBA 3: POST /api/v1/membership-sales - Crear nueva venta de membresía
     */
    @Test
    @DisplayName("POST /api/v1/membership-sales → 201 creado exitosamente")
    void create_Ok() throws Exception {
        // Arrange
        MembershipSaleRequest request = createMembershipSaleRequest(1500.0, 1, 1, 1, 1);
        MembershipSaleResponse response = createMembershipSaleResponse(123, 1500.0, "Juan Pérez", "Membresía Premium", "Gym Central", "Admin User");
        
        when(service.save(any(MembershipSaleRequest.class))).thenReturn(response);

        // Act & Assert
        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.['Sale identifier']").value(123))
                .andExpect(jsonPath("$.payment").value(1500.0))
                .andExpect(jsonPath("$.customerName").value("Juan Pérez"))
                .andExpect(jsonPath("$.membershipName").value("Membresía Premium"))
                .andExpect(jsonPath("$.gymName").value("Gym Central"))
                .andExpect(jsonPath("$.userName").value("Admin User"));
    }

    /*
     * PRUEBA 4: GET /api/v1/membership-sales/customer/{idCustomer} - Búsqueda por ID de cliente
     * Endpoint crítico para el negocio
     */
    @Test
    @DisplayName("GET /api/v1/membership-sales/customer/{idCustomer} → 200 con ventas del cliente")
    void findByCustomerId_Ok() throws Exception {
        // Arrange
        List<MembershipSaleResponse> mockSales = List.of(
            createMembershipSaleResponse(1, 1500.0, "Juan Pérez", "Membresía Premium", "Gym Central", "Admin User"),
            createMembershipSaleResponse(2, 500.0, "Juan Pérez", "Membresía Básica", "Gym Central", "Vendedor User")
        );
        when(service.findByCustomerId(1)).thenReturn(mockSales);

        // Act & Assert
        mvc.perform(get(BASE + "/customer/{idCustomer}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].['Sale identifier']").value(1))
                .andExpect(jsonPath("$[0].customerName").value("Juan Pérez"))
                .andExpect(jsonPath("$[0].payment").value(1500.0))
                .andExpect(jsonPath("$[1].['Sale identifier']").value(2))
                .andExpect(jsonPath("$[1].customerName").value("Juan Pérez"))
                .andExpect(jsonPath("$[1].payment").value(500.0));
    }

    @Test
    @DisplayName("GET /api/v1/membership-sales/customer/{idCustomer} → 200 con lista vacía")
    void findByCustomerId_Empty() throws Exception {
        // Arrange
        when(service.findByCustomerId(999)).thenReturn(Collections.emptyList());

        // Act & Assert
        mvc.perform(get(BASE + "/customer/{idCustomer}", 999))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}