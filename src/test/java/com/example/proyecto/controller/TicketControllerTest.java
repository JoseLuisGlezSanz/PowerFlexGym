package com.example.proyecto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.proyecto.dto.TicketRequest;
import com.example.proyecto.dto.TicketResponse;
import com.example.proyecto.service.TicketService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

    /*
     * ===========================
     * Config de test: mock beans
     * ===========================
     */
    @TestConfiguration
    static class TestConfig {
        @Bean
        TicketService ticketService() {
            return mock(TicketService.class);
        }
    }

    /*
     * ===========================
     * Helpers DTO
     * ===========================
     */
    private TicketResponse resp(Long id, LocalDateTime date, LocalDateTime saleDate, BigDecimal total, 
                               Integer status, Integer methodPayment, BigDecimal paymentWith, 
                               Long customerId, String customerName, Long userId, String userName) {
        return TicketResponse.builder()
                .id(id)
                .date(date)
                .saleDate(saleDate)
                .total(total)
                .status(status)
                .methodPayment(methodPayment)
                .paymentWith(paymentWith)
                .customerId(customerId)
                .customerName(customerName)
                .userId(userId)
                .userName(userName)
                .build();
    }

    private TicketResponse resp(Long id, BigDecimal total, Integer status, Integer methodPayment, 
                               BigDecimal paymentWith, Long customerId, Long userId) {
        LocalDateTime now = LocalDateTime.now();
        return resp(id, now, now, total, status, methodPayment, paymentWith, 
                   customerId, "Customer " + customerId, userId, "User " + userId);
    }

    private TicketRequest req(BigDecimal total, Integer status, Integer methodPayment, 
                             BigDecimal paymentWith, Long customerId, Long userId) {
        TicketRequest r = new TicketRequest();
        r.setTotal(total);
        r.setStatus(status);
        r.setMethodPayment(methodPayment);
        r.setPaymentWith(paymentWith);
        r.setCustomerId(customerId);
        r.setUserId(userId);
        return r;
    }

    private TicketRequest req(BigDecimal total, Integer status, Integer methodPayment) {
        return req(total, status, methodPayment, total.add(new BigDecimal("10.00")), 1L, 1L);
    }

    /*
     * ================================
     * GET /api/v1/tickets
     * ================================
     */

    @Test
    @DisplayName("GET /api/v1/tickets → 200 con lista")
    void findAll_Ok() throws Exception {
        when(service.findAll()).thenReturn(List.of(
            resp(1L, new BigDecimal("99.99"), 1, 1, new BigDecimal("100.00"), 1L, 1L),
            resp(2L, new BigDecimal("149.50"), 1, 2, new BigDecimal("150.00"), 2L, 1L)
        ));

        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].['Ticket identifier']").value(1))
                .andExpect(jsonPath("$[0].total").value(99.99))
                .andExpect(jsonPath("$[0].status").value(1))
                .andExpect(jsonPath("$[0].customerId").value(1))
                .andExpect(jsonPath("$[1].['Ticket identifier']").value(2));
    }

    @Test
    @DisplayName("GET /api/v1/tickets → 200 con lista vacía")
    void findAll_empty() throws Exception {
        when(service.findAll()).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
     * =========================================
     * GET /api/v1/tickets/customer/{customerId}
     * =========================================
     */

    @Test
    @DisplayName("GET /customer/{customerId} → 200 con lista")
    void findAllTicketsByCustomerId_Ok() throws Exception {
        when(service.findAllTicketsByCustomerId(1L)).thenReturn(List.of(
            resp(1L, new BigDecimal("99.99"), 1, 1, new BigDecimal("100.00"), 1L, 1L),
            resp(3L, new BigDecimal("199.99"), 1, 1, new BigDecimal("200.00"), 1L, 2L)
        ));

        mvc.perform(get(BASE + "/customer/{customerId}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].customerId").value(1))
                .andExpect(jsonPath("$[1].customerId").value(1));
    }

    @Test
    @DisplayName("GET /customer/{customerId} sin resultados → 200 con lista vacía")
    void findAllTicketsByCustomerId_empty() throws Exception {
        when(service.findAllTicketsByCustomerId(999L)).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE + "/customer/{customerId}", 999L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
     * =====================================
     * GET /api/v1/tickets/user/{userId}
     * =====================================
     */

    @Test
    @DisplayName("GET /user/{userId} → 200 con lista")
    void findAllTicketsByUserId_Ok() throws Exception {
        when(service.findAllTicketsByUserId(1L)).thenReturn(List.of(
            resp(1L, new BigDecimal("99.99"), 1, 1, new BigDecimal("100.00"), 1L, 1L),
            resp(2L, new BigDecimal("149.50"), 1, 2, new BigDecimal("150.00"), 2L, 1L)
        ));

        mvc.perform(get(BASE + "/user/{userId}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[1].userId").value(1));
    }

    /*
     * =================================
     * GET /api/v1/tickets/{id}
     * =================================
     */

    @Test
    @DisplayName("GET /{id} existente → 200")
    void findById_ok() throws Exception {
        when(service.findById(5L)).thenReturn(
            resp(5L, new BigDecimal("299.99"), 1, 1, new BigDecimal("300.00"), 3L, 2L)
        );

        mvc.perform(get(BASE + "/{id}", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Ticket identifier']").value(5))
                .andExpect(jsonPath("$.total").value(299.99))
                .andExpect(jsonPath("$.status").value(1))
                .andExpect(jsonPath("$.methodPayment").value(1))
                .andExpect(jsonPath("$.paymentWith").value(300.00))
                .andExpect(jsonPath("$.customerId").value(3))
                .andExpect(jsonPath("$.userId").value(2));
    }

    @Test
    @DisplayName("GET /{id} no existente → 404")
    void findById_notFound() throws Exception {
        when(service.findById(999L)).thenThrow(new EntityNotFoundException("Ticket not found"));

        mvc.perform(get(BASE + "/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("GET /{id} no numérico → 400 (binding)")
    void findById_badPath() throws Exception {
        mvc.perform(get(BASE + "/abc"))
                .andExpect(status().isBadRequest());
    }

    /*
     * ================================
     * POST /api/v1/tickets
     * ================================
     */

    @Test
    @DisplayName("POST create válido → 201 + Location + body")
    void create_ok() throws Exception {
        TicketRequest rq = req(new BigDecimal("89.99"), 1, 1, new BigDecimal("100.00"), 1L, 1L);
        TicketResponse created = resp(1234L, new BigDecimal("89.99"), 1, 1, new BigDecimal("100.00"), 1L, 1L);
        when(service.create(any(TicketRequest.class))).thenReturn(created);

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/tickets/1234"))
                .andExpect(jsonPath("$.['Ticket identifier']").value(1234))
                .andExpect(jsonPath("$.total").value(89.99))
                .andExpect(jsonPath("$.status").value(1))
                .andExpect(jsonPath("$.methodPayment").value(1))
                .andExpect(jsonPath("$.paymentWith").value(100.00))
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    @DisplayName("POST create con body inválido → 400")
    void create_invalidBody() throws Exception {
        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    /*
     * ================================
     * PUT /api/v1/tickets/{id}
     * ================================
     */

    @Test
    @DisplayName("PUT update válido → 200 con body actualizado")
    void update_ok() throws Exception {
        TicketRequest rq = req(new BigDecimal("129.99"), 2, 2, new BigDecimal("150.00"), 2L, 3L);
        TicketResponse updated = resp(10L, new BigDecimal("129.99"), 2, 2, new BigDecimal("150.00"), 2L, 3L);
        when(service.update(eq(10L), any(TicketRequest.class))).thenReturn(updated);

        mvc.perform(put(BASE + "/{id}", 10L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Ticket identifier']").value(10))
                .andExpect(jsonPath("$.total").value(129.99))
                .andExpect(jsonPath("$.status").value(2))
                .andExpect(jsonPath("$.methodPayment").value(2))
                .andExpect(jsonPath("$.customerId").value(2))
                .andExpect(jsonPath("$.userId").value(3));
    }

    @Test
    @DisplayName("PUT update en no existente → 404")
    void update_notFound() throws Exception {
        when(service.update(eq(9999L), any(TicketRequest.class)))
                .thenThrow(new EntityNotFoundException("Ticket not found"));

        mvc.perform(put(BASE + "/{id}", 9999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req(new BigDecimal("50.00"), 1, 1))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    /*
     * ======================================
     * GET /api/v1/tickets/paginationAll
     * ======================================
     */

    @ParameterizedTest(name = "GET /paginationAll?page={0}&pageSize={1} → 200")
    @CsvSource({
            "0,10",
            "1,1",
            "2,50",
            "5,5"
    })
    @DisplayName("GET paginationAll: parámetros válidos")
    void paginationAll_ok(int page, int size) throws Exception {
        when(service.getAll(page, size)).thenReturn(List.of(
            resp(100L, new BigDecimal("75.00"), 1, 1, new BigDecimal("80.00"), 10L, 5L),
            resp(101L, new BigDecimal("125.00"), 1, 2, new BigDecimal("130.00"), 11L, 5L)
        ));

        mvc.perform(get(BASE + "/paginationAll")
                .queryParam("page", String.valueOf(page))
                .queryParam("pageSize", String.valueOf(size))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].['Ticket identifier']").value(100))
                .andExpect(jsonPath("$[0].total").value(75.00))
                .andExpect(jsonPath("$[1].['Ticket identifier']").value(101));
    }

    @ParameterizedTest(name = "GET /paginationAll?page={0}&pageSize={1} inválidos → 400")
    @CsvSource({
            "-1,10",
            "0,0",
            "0,-5",
            "-3,-3"
    })
    @DisplayName("GET paginationAll: parámetros inválidos → 400")
    void paginationAll_badRequest(int page, int size) throws Exception {
        when(service.getAll(page, size)).thenThrow(new IllegalArgumentException("Invalid paging params"));

        mvc.perform(get(BASE + "/paginationAll")
                .queryParam("page", String.valueOf(page))
                .queryParam("pageSize", String.valueOf(size)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error", containsStringIgnoringCase("invalid")));
    }

    /*
     * =================================================
     * GET /api/v1/tickets/paginationByCustomerId
     * =================================================
     */

    @Test
    @DisplayName("GET paginationByCustomerId → 200")
    void getAllTicketsByCustomerIdPaginated_ok() throws Exception {
        when(service.getAllTicketsByCustomerId(0, 10, 1L)).thenReturn(List.of(
            resp(1L, new BigDecimal("99.99"), 1, 1, new BigDecimal("100.00"), 1L, 1L),
            resp(3L, new BigDecimal("199.99"), 1, 1, new BigDecimal("200.00"), 1L, 2L)
        ));

        mvc.perform(get(BASE + "/paginationByCustomerId")
                .queryParam("page", "0")
                .queryParam("pageSize", "10")
                .queryParam("customerId", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].customerId").value(1))
                .andExpect(jsonPath("$[1].customerId").value(1));
    }

    /*
     * =============================================
     * GET /api/v1/tickets/paginationByUserId
     * =============================================
     */

    @Test
    @DisplayName("GET paginationByUserId → 200")
    void getAllTicketsByUserIdPaginated_ok() throws Exception {
        when(service.getAllTicketsByUserId(0, 10, 2L)).thenReturn(List.of(
            resp(2L, new BigDecimal("149.50"), 1, 2, new BigDecimal("150.00"), 2L, 2L),
            resp(4L, new BigDecimal("249.99"), 1, 1, new BigDecimal("250.00"), 3L, 2L)
        ));

        mvc.perform(get(BASE + "/paginationByUserId")
                .queryParam("page", "0")
                .queryParam("pageSize", "10")
                .queryParam("userId", "2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userId").value(2))
                .andExpect(jsonPath("$[1].userId").value(2));
    }

    /*
     * =====================================
     * Headers: CORS / Content Negotiation
     * =====================================
     */

    @Test
    @DisplayName("CORS: Access-Control-Allow-Origin")
    void cors_header_present() throws Exception {
        when(service.findAll()).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE).header("Origin", "https://tu-frontend.com").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "https://tu-frontend.com"));
    }

    @Test
    @DisplayName("Content negotiation: Accept JSON → application/json")
    void contentNegotiation_json() throws Exception {
        when(service.findAll()).thenReturn(List.of(resp(1L, new BigDecimal("50.00"), 1, 1, new BigDecimal("60.00"), 1L, 1L)));

        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    /*
     * =====================================
     * Casos especiales
     * =====================================
     */

    @Test
    @DisplayName("POST create con total cero → 201")
    void create_withZeroTotal() throws Exception {
        TicketRequest rq = req(BigDecimal.ZERO, 1, 1, BigDecimal.ZERO, 1L, 1L);
        TicketResponse created = resp(100L, BigDecimal.ZERO, 1, 1, BigDecimal.ZERO, 1L, 1L);
        when(service.create(any(TicketRequest.class))).thenReturn(created);

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.total").value(0.0))
                .andExpect(jsonPath("$.paymentWith").value(0.0));
    }

    @Test
    @DisplayName("PUT update con status cancelado → 200")
    void update_withCanceledStatus() throws Exception {
        TicketRequest rq = req(new BigDecimal("99.99"), 0, 1, new BigDecimal("100.00"), 1L, 1L);
        TicketResponse updated = resp(20L, new BigDecimal("99.99"), 0, 1, new BigDecimal("100.00"), 1L, 1L);
        when(service.update(eq(20L), any(TicketRequest.class))).thenReturn(updated);

        mvc.perform(put(BASE + "/{id}", 20L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(0));
    }

    @Test
    @DisplayName("GET /customer/{customerId} con múltiples tickets → 200")
    void findAllTicketsByCustomerId_multiple() throws Exception {
        when(service.findAllTicketsByCustomerId(5L)).thenReturn(List.of(
            resp(1L, new BigDecimal("50.00"), 1, 1, new BigDecimal("60.00"), 5L, 1L),
            resp(2L, new BigDecimal("75.00"), 1, 2, new BigDecimal("80.00"), 5L, 2L),
            resp(3L, new BigDecimal("100.00"), 1, 1, new BigDecimal("100.00"), 5L, 1L)
        ));

        mvc.perform(get(BASE + "/customer/{customerId}", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].customerId").value(5))
                .andExpect(jsonPath("$[1].customerId").value(5))
                .andExpect(jsonPath("$[2].customerId").value(5));
    }

    @Test
    @DisplayName("POST create con paymentWith menor que total → 400 (depende de validación)")
    void create_withInsufficientPayment() throws Exception {
        TicketRequest rq = req(new BigDecimal("100.00"), 1, 1, new BigDecimal("90.00"), 1L, 1L);

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET paginationAll sin parámetros → usa valores por defecto")
    void paginationAll_defaultParams() throws Exception {
        when(service.getAll(0, 10)).thenReturn(List.of(resp(1L, new BigDecimal("25.00"), 1, 1, new BigDecimal("30.00"), 1L, 1L)));

        mvc.perform(get(BASE + "/paginationAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].total").value(25.00));
    }
}