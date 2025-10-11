package com.example.proyecto.mapper;

import com.example.proyecto.dto.ProductRequest;
import com.example.proyecto.dto.ProductResponse;
import com.example.proyecto.model.Product;

public class ProductMapper {
    public static ProductResponse toResponse(Product product) {
        if (product == null)
            return null;
        return ProductResponse.builder()
                .idProduct(product.getIdProduct())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .status(product.getStatus())
                .photo(product.getPhoto())
                .build();
    }

    public static Product toEntity(ProductRequest dto) {
        if (dto == null)
            return null;
        return Product.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .status(dto.getStatus())
                .photo(dto.getPhoto())
                .build();
    }

    public static void copyToEntity(ProductRequest dto, Product entity) {
        if (dto == null || entity == null)
            return;
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setStock(dto.getStock());
        entity.setStatus(dto.getStatus());
        entity.setPhoto(dto.getPhoto());
    }
}