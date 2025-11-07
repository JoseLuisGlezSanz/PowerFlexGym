package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;

import com.example.proyecto.dto.ProductRequest;
import com.example.proyecto.dto.ProductResponse;
import com.example.proyecto.model.Product;
import com.example.proyecto.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Provides methods for managing products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Get all products")
    @ApiResponse(responseCode = "200", description = "List of all products", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Product.class)))})
    public List<ProductResponse> findAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    @ApiResponse(responseCode = "200", description = "Product by ID", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Product.class)))})
    public ProductResponse findById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new product")
    @ApiResponse(responseCode = "200", description = "Product create", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Product.class)))})
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest productRequest) {
        ProductResponse createdProduct = productService.create(productRequest);
        return ResponseEntity
                .created(URI.create("/api/v1/products/" + createdProduct.getId()))
                .body(createdProduct);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update product by ID")
    @ApiResponse(responseCode = "200", description = "Product updated successfully", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Product.class)))})
    public ProductResponse update(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        return productService.update(id, productRequest);
    }

    // @DeleteMapping("/{id}")
    // @Operation(summary = "Delete product by ID")
    // @ApiResponse(responseCode = "200", description = "Product deleted successfully", 
    //         content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Product.class)))})
    // public Void delete(@PathVariable Long id) {
    //     return productService.delete(id);
    // }

    @GetMapping(value = "paginationAll", params = { "page", "pageSize" })
    @Operation(summary = "Get all products with pagination")
    @ApiResponse(responseCode = "200", description = "List of all products paginated", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Product.class)))})
    public List<ProductResponse> getAllPaginated(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        List<ProductResponse> products = productService.getAll(page, pageSize);
        return products;
    }

    @GetMapping("/{name}")
    @Operation(summary = "Get products by name")
    @ApiResponse(responseCode = "200", description = "List of products by name", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Product.class)))})
    public List<ProductResponse> findByName(@PathVariable String name) {
        return productService.findByName(name);
    }

    @GetMapping("/{status}")
    @Operation(summary = "Get products by status")
    @ApiResponse(responseCode = "200", description = "List of products by status", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Product.class)))})
    public List<ProductResponse> findByStatus(@PathVariable Integer status) {
        return productService.findByStatus(status);
    }

    @GetMapping("/{stock}")
    @Operation(summary = "Get products with low stock")
    @ApiResponse(responseCode = "200", description = "List of products by low stock", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Product.class)))})
    public List<ProductResponse> findByStockLessThan(@PathVariable Integer stock) {
        return productService.findByStockLessThan(stock);
    }
}