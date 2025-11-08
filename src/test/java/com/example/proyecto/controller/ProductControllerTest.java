package com.example.proyecto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.proyecto.dto.ProductRequest;
import com.example.proyecto.dto.ProductResponse;
import com.example.proyecto.service.ProductService;
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

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    @Autowired
    MockMvc mvc;
    
    @Autowired
    ObjectMapper mapper;

    @Autowired
    ProductService service;

    private static final String BASE = "/api/v1/products";

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
        ProductService productService() {
            return mock(ProductService.class);
        }
    }

    /*
     * ===========================
     * Helpers DTO
     * ===========================
     */
    private ProductResponse resp(Long id, String name, BigDecimal price, Integer stock, Integer status, String photo) {
        return ProductResponse.builder()
                .id(id)
                .name(name)
                .price(price)
                .stock(stock)
                .status(status)
                .photo(photo)
                .build();
    }

    private ProductResponse resp(Long id, String name, BigDecimal price, Integer stock) {
        return resp(id, name, price, stock, 1, "product.jpg");
    }

    private ProductRequest req(String name, BigDecimal price, Integer stock, Integer status, String photo) {
        ProductRequest r = new ProductRequest();
        r.setName(name);
        r.setPrice(price);
        r.setStock(stock);
        r.setStatus(status);
        r.setPhoto(photo);
        return r;
    }

    private ProductRequest req(String name, BigDecimal price, Integer stock) {
        return req(name, price, stock, 1, "product.jpg");
    }

    /*
     * ================================
     * GET /api/v1/products
     * ================================
     */

    @Test
    @DisplayName("GET /api/v1/products → 200 con lista")
    void findAll_Ok() throws Exception {
        when(service.findAll()).thenReturn(List.of(
            resp(1L, "Protein Powder", new BigDecimal("49.99"), 50),
            resp(2L, "Water Bottle", new BigDecimal("15.50"), 100)
        ));

        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].['Product identifier']").value(1))
                .andExpect(jsonPath("$[0].name").value("Protein Powder"))
                .andExpect(jsonPath("$[0].price").value(49.99))
                .andExpect(jsonPath("$[0].stock").value(50))
                .andExpect(jsonPath("$[1].['Product identifier']").value(2));
    }

    @Test
    @DisplayName("GET /api/v1/products → 200 con lista vacía")
    void findAll_empty() throws Exception {
        when(service.findAll()).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
     * ========================================
     * GET /api/v1/products/name/{name}
     * ========================================
     */

    @Test
    @DisplayName("GET /name/{name} → 200 con lista")
    void findByName_Ok() throws Exception {
        when(service.findByName("Protein")).thenReturn(List.of(
            resp(1L, "Protein Powder", new BigDecimal("49.99"), 50),
            resp(3L, "Protein Bar", new BigDecimal("2.99"), 200)
        ));

        mvc.perform(get(BASE + "/name/{name}", "Protein").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Protein Powder"))
                .andExpect(jsonPath("$[1].name").value("Protein Bar"));
    }

    @Test
    @DisplayName("GET /name/{name} sin resultados → 200 con lista vacía")
    void findByName_empty() throws Exception {
        when(service.findByName("NonExistent")).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE + "/name/{name}", "NonExistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
     * ========================================
     * GET /api/v1/products/status/{status}
     * ========================================
     */

    @Test
    @DisplayName("GET /status/{status} → 200 con lista")
    void findByStatus_Ok() throws Exception {
        when(service.findByStatus(1)).thenReturn(List.of(
            resp(1L, "Active Product", new BigDecimal("25.00"), 10, 1, "active.jpg"),
            resp(2L, "Another Active", new BigDecimal("30.00"), 20, 1, "photo.jpg")
        ));

        mvc.perform(get(BASE + "/status/{status}", 1).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].status").value(1))
                .andExpect(jsonPath("$[1].status").value(1));
    }

    @Test
    @DisplayName("GET /status/{status} inactivo → 200 con lista")
    void findByStatus_inactive() throws Exception {
        when(service.findByStatus(0)).thenReturn(List.of(
            resp(5L, "Inactive Product", new BigDecimal("10.00"), 0, 0, "inactive.jpg")
        ));

        mvc.perform(get(BASE + "/status/{status}", 0))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value(0));
    }

    /*
     * ========================================
     * GET /api/v1/products/stock/{stock}
     * ========================================
     */

    @Test
    @DisplayName("GET /stock/{stock} → 200 con lista de bajo stock")
    void findByStockLessThan_Ok() throws Exception {
        when(service.findByStockLessThan(10)).thenReturn(List.of(
            resp(1L, "Low Stock Item", new BigDecimal("99.99"), 5, 1, "lowstock.jpg"),
            resp(2L, "Almost Out", new BigDecimal("19.99"), 2, 1, "photo.jpg")
        ));

        mvc.perform(get(BASE + "/stock/{stock}", 10).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].stock").value(5))
                .andExpect(jsonPath("$[1].stock").value(2));
    }

    @Test
    @DisplayName("GET /stock/{stock} sin resultados → 200 con lista vacía")
    void findByStockLessThan_empty() throws Exception {
        when(service.findByStockLessThan(5)).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE + "/stock/{stock}", 5))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
     * =================================
     * GET /api/v1/products/{id}
     * =================================
     */

    @Test
    @DisplayName("GET /{id} existente → 200")
    void findById_ok() throws Exception {
        when(service.findById(5L)).thenReturn(resp(5L, "Premium Product", new BigDecimal("199.99"), 25, 1, "premium.jpg"));

        mvc.perform(get(BASE + "/{id}", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Product identifier']").value(5))
                .andExpect(jsonPath("$.name").value("Premium Product"))
                .andExpect(jsonPath("$.price").value(199.99))
                .andExpect(jsonPath("$.stock").value(25))
                .andExpect(jsonPath("$.status").value(1))
                .andExpect(jsonPath("$.photo").value("premium.jpg"));
    }

    @Test
    @DisplayName("GET /{id} no existente → 404")
    void findById_notFound() throws Exception {
        when(service.findById(999L)).thenThrow(new EntityNotFoundException("Product not found"));

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
     * POST /api/v1/products
     * ================================
     */

    @Test
    @DisplayName("POST create válido → 201 + Location + body")
    void create_ok() throws Exception {
        ProductRequest rq = req("New Product", new BigDecimal("29.99"), 100, 1, "new.jpg");
        ProductResponse created = resp(1234L, "New Product", new BigDecimal("29.99"), 100, 1, "new.jpg");
        when(service.create(any(ProductRequest.class))).thenReturn(created);

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/products/1234"))
                .andExpect(jsonPath("$.['Product identifier']").value(1234))
                .andExpect(jsonPath("$.name").value("New Product"))
                .andExpect(jsonPath("$.price").value(29.99))
                .andExpect(jsonPath("$.stock").value(100))
                .andExpect(jsonPath("$.status").value(1))
                .andExpect(jsonPath("$.photo").value("new.jpg"));
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
        ProductRequest rq = req("", BigDecimal.ZERO, 0, 0, "");

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isBadRequest());
    }

    /*
     * ================================
     * PUT /api/v1/products/{id}
     * ================================
     */

    @Test
    @DisplayName("PUT update válido → 200 con body actualizado")
    void update_ok() throws Exception {
        ProductRequest rq = req("Updated Product", new BigDecimal("39.99"), 75, 1, "updated.jpg");
        ProductResponse updated = resp(10L, "Updated Product", new BigDecimal("39.99"), 75, 1, "updated.jpg");
        when(service.update(eq(10L), any(ProductRequest.class))).thenReturn(updated);

        mvc.perform(put(BASE + "/{id}", 10L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Product identifier']").value(10))
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.price").value(39.99))
                .andExpect(jsonPath("$.stock").value(75))
                .andExpect(jsonPath("$.photo").value("updated.jpg"));
    }

    @Test
    @DisplayName("PUT update en no existente → 404")
    void update_notFound() throws Exception {
        when(service.update(eq(9999L), any(ProductRequest.class)))
                .thenThrow(new EntityNotFoundException("Product not found"));

        mvc.perform(put(BASE + "/{id}", 9999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req("Test", new BigDecimal("9.99"), 10))))
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
     * ======================================
     * GET /api/v1/products/paginationAll
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
            resp(100L, "Paged Product 1", new BigDecimal("25.00"), 50),
            resp(101L, "Paged Product 2", new BigDecimal("35.00"), 60)
        ));

        mvc.perform(get(BASE + "/paginationAll")
                .queryParam("page", String.valueOf(page))
                .queryParam("pageSize", String.valueOf(size))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].['Product identifier']").value(100))
                .andExpect(jsonPath("$[0].name").value("Paged Product 1"))
                .andExpect(jsonPath("$[1].['Product identifier']").value(101));
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
        when(service.getAll(0, 10)).thenReturn(List.of(resp(1L, "Default Product", new BigDecimal("10.00"), 25)));

        mvc.perform(get(BASE + "/paginationAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Default Product"));
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
        when(service.findAll()).thenReturn(List.of(resp(1L, "Test Product", new BigDecimal("19.99"), 30)));

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
    @DisplayName("GET /name/{name} con nombre con espacios → 200")
    void findByName_withSpaces() throws Exception {
        when(service.findByName("Energy Drink")).thenReturn(List.of(
            resp(4L, "Energy Drink", new BigDecimal("3.99"), 150)
        ));

        mvc.perform(get(BASE + "/name/{name}", "Energy Drink"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Energy Drink"));
    }

    @Test
    @DisplayName("POST create con precio cero → 201")
    void create_withZeroPrice() throws Exception {
        ProductRequest rq = req("Free Sample", BigDecimal.ZERO, 1000, 1, "free.jpg");
        ProductResponse created = resp(100L, "Free Sample", BigDecimal.ZERO, 1000, 1, "free.jpg");
        when(service.create(any(ProductRequest.class))).thenReturn(created);

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.price").value(0.0));
    }

    @Test
    @DisplayName("PUT update con stock cero → 200")
    void update_withZeroStock() throws Exception {
        ProductRequest rq = req("Out of Stock", new BigDecimal("5.99"), 0, 1, "out.jpg");
        ProductResponse updated = resp(20L, "Out of Stock", new BigDecimal("5.99"), 0, 1, "out.jpg");
        when(service.update(eq(20L), any(ProductRequest.class))).thenReturn(updated);

        mvc.perform(put(BASE + "/{id}", 20L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock").value(0));
    }

    @Test
    @DisplayName("GET /stock/{stock} con stock cero → 200")
    void findByStockLessThan_zero() throws Exception {
        when(service.findByStockLessThan(1)).thenReturn(List.of(
            resp(6L, "Out of Stock", new BigDecimal("9.99"), 0, 1, "out.jpg")
        ));

        mvc.perform(get(BASE + "/stock/{stock}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].stock").value(0));
    }

    @Test
    @DisplayName("POST create con precio negativo → 400")
    void create_withNegativePrice() throws Exception {
        ProductRequest rq = req("Invalid Product", new BigDecimal("-10.00"), 50, 1, "invalid.jpg");

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST create con stock negativo → 400")
    void create_withNegativeStock() throws Exception {
        ProductRequest rq = req("Invalid Stock", new BigDecimal("10.00"), -5, 1, "stock.jpg");

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isBadRequest());
    }
}