package com.example.proyecto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.proyecto.dto.TicketDetailRequest;
import com.example.proyecto.dto.TicketDetailResponse;
import com.example.proyecto.service.TicketDetailService;
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
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

    /*
     * ===========================
     * Config de test: mock beans
     * ===========================
     */
    @TestConfiguration
    static class TestConfig {
        @Bean
        TicketDetailService ticketDetailService() {
            return mock(TicketDetailService.class);
        }
    }

    /*
     * ===========================
     * Helpers DTO
     * ===========================
     */
    private TicketDetailResponse resp(Long id, Integer amount, BigDecimal unitPrice, BigDecimal subtotal, 
                                     Long productId, String productName, Long ticketId) {
        return TicketDetailResponse.builder()
                .id(id)
                .amount(amount)
                .unitPrice(unitPrice)
                .subtotal(subtotal)
                .productId(productId)
                .productName(productName)
                .ticketId(ticketId)
                .build();
    }

    private TicketDetailResponse resp(Long id, Integer amount, BigDecimal unitPrice, Long productId, Long ticketId) {
        BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(amount));
        return resp(id, amount, unitPrice, subtotal, productId, "Product " + productId, ticketId);
    }

    private TicketDetailRequest req(Integer amount, Long productId, Long ticketId) {
        TicketDetailRequest r = new TicketDetailRequest();
        r.setAmount(amount);
        r.setProductId(productId);
        r.setTicketId(ticketId);
        return r;
    }

    /*
     * ========================================
     * GET /api/v1/ticket-details
     * ========================================
     */

    @Test
    @DisplayName("GET /api/v1/ticket-details → 200 con lista")
    void findAll_Ok() throws Exception {
        when(service.findAll()).thenReturn(List.of(
            resp(1L, 2, new BigDecimal("25.00"), 1L, 1L),
            resp(2L, 1, new BigDecimal("50.00"), 2L, 1L),
            resp(3L, 3, new BigDecimal("15.00"), 3L, 2L)
        ));

        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].['Detail identifier']").value(1))
                .andExpect(jsonPath("$[0].amount").value(2))
                .andExpect(jsonPath("$[0].unitPrice").value(25.00))
                .andExpect(jsonPath("$[0].subtotal").value(50.00))
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].ticketId").value(1))
                .andExpect(jsonPath("$[1].['Detail identifier']").value(2));
    }

    @Test
    @DisplayName("GET /api/v1/ticket-details → 200 con lista vacía")
    void findAll_empty() throws Exception {
        when(service.findAll()).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
     * =================================================
     * GET /api/v1/ticket-details/ticket/{ticketId}
     * =================================================
     */

    @Test
    @DisplayName("GET /ticket/{ticketId} → 200 con lista")
    void findByTicketId_Ok() throws Exception {
        when(service.findByTicketId(1L)).thenReturn(List.of(
            resp(1L, 2, new BigDecimal("25.00"), 1L, 1L),
            resp(2L, 1, new BigDecimal("50.00"), 2L, 1L)
        ));

        mvc.perform(get(BASE + "/ticket/{ticketId}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].ticketId").value(1))
                .andExpect(jsonPath("$[0].amount").value(2))
                .andExpect(jsonPath("$[1].ticketId").value(1))
                .andExpect(jsonPath("$[1].amount").value(1));
    }

    @Test
    @DisplayName("GET /ticket/{ticketId} sin resultados → 200 con lista vacía")
    void findByTicketId_empty() throws Exception {
        when(service.findByTicketId(999L)).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE + "/ticket/{ticketId}", 999L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("GET /ticket/{ticketId} con ID no numérico → 400")
    void findByTicketId_badPath() throws Exception {
        mvc.perform(get(BASE + "/ticket/abc"))
                .andExpect(status().isBadRequest());
    }

    /*
     * ========================================
     * GET /api/v1/ticket-details/{id}
     * ========================================
     */

    @Test
    @DisplayName("GET /{id} existente → 200")
    void findById_ok() throws Exception {
        when(service.findById(5L)).thenReturn(
            resp(5L, 5, new BigDecimal("10.00"), 3L, 2L)
        );

        mvc.perform(get(BASE + "/{id}", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Detail identifier']").value(5))
                .andExpect(jsonPath("$.amount").value(5))
                .andExpect(jsonPath("$.unitPrice").value(10.00))
                .andExpect(jsonPath("$.subtotal").value(50.00))
                .andExpect(jsonPath("$.productId").value(3))
                .andExpect(jsonPath("$.ticketId").value(2));
    }

    @Test
    @DisplayName("GET /{id} no existente → 404")
    void findById_notFound() throws Exception {
        when(service.findById(999L)).thenThrow(new EntityNotFoundException("Ticket detail not found"));

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
     * ========================================
     * POST /api/v1/ticket-details
     * ========================================
     */

    @Test
    @DisplayName("POST create válido → 201 + Location + body")
    void create_ok() throws Exception {
        TicketDetailRequest rq = req(3, 1L, 1L);
        TicketDetailResponse created = resp(1234L, 3, new BigDecimal("15.00"), 1L, 1L);
        when(service.create(any(TicketDetailRequest.class))).thenReturn(created);

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/ticket-details/1234"))
                .andExpect(jsonPath("$.['Detail identifier']").value(1234))
                .andExpect(jsonPath("$.amount").value(3))
                .andExpect(jsonPath("$.unitPrice").value(15.00))
                .andExpect(jsonPath("$.subtotal").value(45.00))
                .andExpect(jsonPath("$.productId").value(1))
                .andExpect(jsonPath("$.ticketId").value(1));
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

    @Test
    @DisplayName("POST create con campos vacíos → 400")
    void create_emptyFields() throws Exception {
        TicketDetailRequest rq = req(null, null, null);

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isBadRequest());
    }

    /*
     * ========================================
     * PUT /api/v1/ticket-details/{id}
     * ========================================
     */

    @Test
    @DisplayName("PUT update válido → 200 con body actualizado")
    void update_ok() throws Exception {
        TicketDetailRequest rq = req(5, 2L, 3L);
        TicketDetailResponse updated = resp(10L, 5, new BigDecimal("20.00"), 2L, 3L);
        when(service.update(eq(10L), any(TicketDetailRequest.class))).thenReturn(updated);

        mvc.perform(put(BASE + "/{id}", 10L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Detail identifier']").value(10))
                .andExpect(jsonPath("$.amount").value(5))
                .andExpect(jsonPath("$.unitPrice").value(20.00))
                .andExpect(jsonPath("$.subtotal").value(100.00))
                .andExpect(jsonPath("$.productId").value(2))
                .andExpect(jsonPath("$.ticketId").value(3));
    }

    @Test
    @DisplayName("PUT update en no existente → 404")
    void update_notFound() throws Exception {
        when(service.update(eq(9999L), any(TicketDetailRequest.class)))
                .thenThrow(new EntityNotFoundException("Ticket detail not found"));

        mvc.perform(put(BASE + "/{id}", 9999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req(1, 1L, 1L))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("PUT update con body inválido → 400")
    void update_invalidBody() throws Exception {
        mvc.perform(put(BASE + "/{id}", 10L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    /*
     * =================================================
     * GET /api/v1/ticket-details/paginationAll
     * =================================================
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
            resp(100L, 2, new BigDecimal("30.00"), 4L, 5L),
            resp(101L, 1, new BigDecimal("75.00"), 5L, 5L)
        ));

        mvc.perform(get(BASE + "/paginationAll")
                .queryParam("page", String.valueOf(page))
                .queryParam("pageSize", String.valueOf(size))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].['Detail identifier']").value(100))
                .andExpect(jsonPath("$[0].amount").value(2))
                .andExpect(jsonPath("$[0].subtotal").value(60.00))
                .andExpect(jsonPath("$[1].['Detail identifier']").value(101));
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

    @Test
    @DisplayName("GET paginationAll sin parámetros → usa valores por defecto")
    void paginationAll_defaultParams() throws Exception {
        when(service.getAll(0, 10)).thenReturn(List.of(resp(1L, 1, new BigDecimal("10.00"), 1L, 1L)));

        mvc.perform(get(BASE + "/paginationAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].amount").value(1));
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
        when(service.findAll()).thenReturn(List.of(resp(1L, 1, new BigDecimal("25.00"), 1L, 1L)));

        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Content negotiation: Accept XML → 406")
    void contentNegotiation_xml() throws Exception {
        mvc.perform(get(BASE).accept(MediaType.APPLICATION_XML))
                .andExpect(status().isNotAcceptable());
    }

    /*
     * =====================================
     * Casos especiales
     * =====================================
     */

    @Test
    @DisplayName("POST create con cantidad cero → 400")
    void create_withZeroAmount() throws Exception {
        TicketDetailRequest rq = req(0, 1L, 1L);

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST create con cantidad negativa → 400")
    void create_withNegativeAmount() throws Exception {
        TicketDetailRequest rq = req(-1, 1L, 1L);

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT update con cantidad grande → 200")
    void update_withLargeAmount() throws Exception {
        TicketDetailRequest rq = req(100, 1L, 1L);
        TicketDetailResponse updated = resp(20L, 100, new BigDecimal("1.50"), 1L, 1L);
        when(service.update(eq(20L), any(TicketDetailRequest.class))).thenReturn(updated);

        mvc.perform(put(BASE + "/{id}", 20L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(100))
                .andExpect(jsonPath("$.subtotal").value(150.00));
    }

    @Test
    @DisplayName("GET /ticket/{ticketId} con múltiples detalles → 200")
    void findByTicketId_multiple() throws Exception {
        when(service.findByTicketId(10L)).thenReturn(List.of(
            resp(1L, 2, new BigDecimal("25.00"), 1L, 10L),
            resp(2L, 1, new BigDecimal("50.00"), 2L, 10L),
            resp(3L, 3, new BigDecimal("15.00"), 3L, 10L),
            resp(4L, 1, new BigDecimal("100.00"), 4L, 10L)
        ));

        mvc.perform(get(BASE + "/ticket/{ticketId}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].ticketId").value(10))
                .andExpect(jsonPath("$[1].ticketId").value(10))
                .andExpect(jsonPath("$[2].ticketId").value(10))
                .andExpect(jsonPath("$[3].ticketId").value(10));
    }

    @Test
    @DisplayName("POST create con precio unitario cero → 201 (si el servicio lo permite)")
    void create_withZeroUnitPrice() throws Exception {
        TicketDetailRequest rq = req(1, 1L, 1L);
        TicketDetailResponse created = resp(100L, 1, BigDecimal.ZERO, 1L, 1L);
        when(service.create(any(TicketDetailRequest.class))).thenReturn(created);

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.unitPrice").value(0.0))
                .andExpect(jsonPath("$.subtotal").value(0.0));
    }

    @Test
    @DisplayName("GET paginationAll sin resultados → 200 con lista vacía")
    void paginationAll_empty() throws Exception {
        when(service.getAll(0, 10)).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE + "/paginationAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}