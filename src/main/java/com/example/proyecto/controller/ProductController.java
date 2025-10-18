package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
    @ApiResponse(responseCode = "200", description = "List of all products", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Product.class)))})
    public ResponseEntity<List<ProductResponse>> findAll() {
        List<ProductResponse> products = productService.findAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    @ApiResponse(responseCode = "200", description = "Product found", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Product.class)))})
    public ResponseEntity<ProductResponse> findById(@PathVariable Integer id) {
        ProductResponse product = productService.findById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    @Operation(summary = "Create a new product")
    @ApiResponse(responseCode = "200", description = "Product created successfully", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Product.class)))})
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest productRequest) {
        ProductResponse createdProduct = productService.save(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product by ID")
    @ApiResponse(responseCode = "200", description = "Product updated successfully", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Product.class)))})
    public ResponseEntity<ProductResponse> update(@PathVariable Integer id, @RequestBody ProductRequest productRequest) {
        ProductResponse updatedProduct = productService.update(id, productRequest);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product by ID")
    @ApiResponse(responseCode = "200", description = "Product deleted successfully", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Product.class)))})
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search products by name")
    @ApiResponse(responseCode = "200", description = "List of products matching the name", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Product.class)))})
    public List<ProductResponse> findByName(@PathVariable String name) {
        return productService.findByName(name);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get products by status")
    @ApiResponse(responseCode = "200", description = "List of products with the specified status",
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Product.class)))})
    public List<ProductResponse> findByStatus(@PathVariable Integer status) {
        return productService.findByStatus(status);
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Get products with low stock")
    @ApiResponse(responseCode = "200", description = "List of products with stock less than specified value",
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Product.class)))})
    public List<ProductResponse> findByStockLessThan(@PathVariable Integer stock) {
        return productService.findByStockLessThan(stock);
    }
}