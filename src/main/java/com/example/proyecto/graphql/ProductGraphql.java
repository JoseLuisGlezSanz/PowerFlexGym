package com.example.proyecto.graphql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.example.proyecto.dto.ProductRequest;
import com.example.proyecto.dto.ProductResponse;
import com.example.proyecto.service.ProductService;

@Controller
public class ProductGraphql {
    @Autowired
    private ProductService productService;

    @QueryMapping
    public List<ProductResponse> findAllProducts() {
        return productService.findAll();
    }

    @QueryMapping
    public ProductResponse findByIdProduct(@Argument Long id) {
        return productService.findById(id);
    }

    @MutationMapping
    public ProductResponse createProduct(@Argument ProductRequest productRequest) {
        return productService.create(productRequest);
    }

    @MutationMapping
    public ProductResponse updateProduct(@Argument Long id, @Argument ProductRequest productRequest) {
        return productService.update(id, productRequest);
    }

    @QueryMapping
    public List<ProductResponse> findAllProductsPaginated(@Argument int page, @Argument int pageSize) {
        return productService.getAll(page, pageSize);
    }

    @QueryMapping
    public List<ProductResponse> findByNameProduct(@Argument String name) {
        return productService.findByName(name);
    }

    @QueryMapping
    public List<ProductResponse> findByStatusProduct(@Argument Integer status) {
        return productService.findByStatus(status);
    }

    @QueryMapping
    public List<ProductResponse> findByStockProduct(@Argument Integer stock) {
        return productService.findByStockLessThan(stock);
    }
}