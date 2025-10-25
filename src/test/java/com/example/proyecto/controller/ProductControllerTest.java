package com.example.proyecto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.proyecto.dto.ProductRequest;
import com.example.proyecto.dto.ProductResponse;
import com.example.proyecto.service.ProductService;
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

    @TestConfiguration
    static class TestConfig {
        @Bean
        ProductService productService() {
            return mock(ProductService.class);
        }
    }

    // Helpers para crear DTOs de prueba
    private ProductResponse createProductResponse(Integer id, String name, BigDecimal price, Integer stock, Integer status) {
        return ProductResponse.builder()
                .idProduct(id)
                .name(name)
                .price(price)
                .stock(stock)
                .status(status)
                .photo("product_" + id + ".jpg")
                .build();
    }

    private ProductRequest createProductRequest(String name, BigDecimal price, Integer stock, Integer status) {
        ProductRequest request = new ProductRequest();
        request.setName(name);
        request.setPrice(price);
        request.setStock(stock);
        request.setStatus(status);
        request.setPhoto("product.jpg");
        return request;
    }

    /*
     * PRUEBA 1: GET /api/v1/products - Obtener todos los productos
     */
    @Test
    @DisplayName("GET /api/v1/products → 200 con lista de productos")
    void findAll_Ok() throws Exception {
        // Arrange
        List<ProductResponse> mockProducts = List.of(
            createProductResponse(1, "Proteína Whey", new BigDecimal("899.99"), 50, 1),
            createProductResponse(2, "Creatina 300g", new BigDecimal("499.50"), 25, 1),
            createProductResponse(3, "Shaker", new BigDecimal("199.99"), 100, 1)
        );
        when(service.findAll()).thenReturn(mockProducts);

        // Act & Assert
        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].['Product identifier']").value(1))
                .andExpect(jsonPath("$[0].name").value("Proteína Whey"))
                .andExpect(jsonPath("$[0].price").value(899.99))
                .andExpect(jsonPath("$[0].stock").value(50))
                .andExpect(jsonPath("$[0].status").value(1))
                .andExpect(jsonPath("$[1].['Product identifier']").value(2))
                .andExpect(jsonPath("$[1].name").value("Creatina 300g"))
                .andExpect(jsonPath("$[1].price").value(499.50))
                .andExpect(jsonPath("$[2].['Product identifier']").value(3))
                .andExpect(jsonPath("$[2].name").value("Shaker"))
                .andExpect(jsonPath("$[2].price").value(199.99));
    }

    @Test
    @DisplayName("GET /api/v1/products → 200 con lista vacía")
    void findAll_Empty() throws Exception {
        // Arrange
        when(service.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
     * PRUEBA 2: GET /api/v1/products/{id} - Obtener producto por ID
     */
    @Test
    @DisplayName("GET /api/v1/products/{id} existente → 200")
    void findById_Ok() throws Exception {
        // Arrange
        ProductResponse mockProduct = createProductResponse(1, "Proteína Whey", new BigDecimal("899.99"), 50, 1);
        when(service.findById(1)).thenReturn(mockProduct);

        // Act & Assert
        mvc.perform(get(BASE + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Product identifier']").value(1))
                .andExpect(jsonPath("$.name").value("Proteína Whey"))
                .andExpect(jsonPath("$.price").value(899.99))
                .andExpect(jsonPath("$.stock").value(50))
                .andExpect(jsonPath("$.status").value(1))
                .andExpect(jsonPath("$.photo").value("product_1.jpg"));
    }

    @Test
    @DisplayName("GET /api/v1/products/{id} no existente → 404")
    void findById_NotFound() throws Exception {
        // Arrange
        when(service.findById(999)).thenThrow(new EntityNotFoundException("Product not found"));

        // Act & Assert
        mvc.perform(get(BASE + "/{id}", 999))
                .andExpect(status().isNotFound());
    }

    /*
     * PRUEBA 3: POST /api/v1/products - Crear nuevo producto
     */
    @Test
    @DisplayName("POST /api/v1/products → 201 creado exitosamente")
    void create_Ok() throws Exception {
        // Arrange
        ProductRequest request = createProductRequest("Nueva Proteína", new BigDecimal("799.99"), 30, 1);
        ProductResponse response = createProductResponse(123, "Nueva Proteína", new BigDecimal("799.99"), 30, 1);
        
        when(service.save(any(ProductRequest.class))).thenReturn(response);

        // Act & Assert
        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.['Product identifier']").value(123))
                .andExpect(jsonPath("$.name").value("Nueva Proteína"))
                .andExpect(jsonPath("$.price").value(799.99))
                .andExpect(jsonPath("$.stock").value(30))
                .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    @DisplayName("POST /api/v1/products con datos inválidos → 400")
    void create_InvalidBody() throws Exception {
        // Arrange - Body sin campos requeridos
        ProductRequest invalidRequest = new ProductRequest(); // Todos los campos null

        // Act & Assert
        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    /*
     * PRUEBA 4: GET /api/v1/products/low-stock/{stock} - Productos con stock bajo
     * Endpoint crítico para gestión de inventario
     */
    @Test
    @DisplayName("GET /api/v1/products/low-stock/{stock} → 200 con productos de stock bajo")
    void findByStockLessThan_Ok() throws Exception {
        // Arrange
        List<ProductResponse> mockProducts = List.of(
            createProductResponse(1, "Proteína Whey", new BigDecimal("899.99"), 5, 1),
            createProductResponse(2, "Creatina 300g", new BigDecimal("499.50"), 2, 1)
        );
        when(service.findByStockLessThan(10)).thenReturn(mockProducts);

        // Act & Assert
        mvc.perform(get(BASE + "/low-stock/{stock}", 10))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].['Product identifier']").value(1))
                .andExpect(jsonPath("$[0].name").value("Proteína Whey"))
                .andExpect(jsonPath("$[0].stock").value(5))
                .andExpect(jsonPath("$[1].['Product identifier']").value(2))
                .andExpect(jsonPath("$[1].name").value("Creatina 300g"))
                .andExpect(jsonPath("$[1].stock").value(2));
    }

    @Test
    @DisplayName("GET /api/v1/products/low-stock/{stock} → 200 con lista vacía")
    void findByStockLessThan_Empty() throws Exception {
        // Arrange
        when(service.findByStockLessThan(5)).thenReturn(Collections.emptyList());

        // Act & Assert
        mvc.perform(get(BASE + "/low-stock/{stock}", 5))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}