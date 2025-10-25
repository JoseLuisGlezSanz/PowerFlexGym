package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import com.example.proyecto.dto.MembershipSaleRequest;
import com.example.proyecto.dto.MembershipSaleResponse;
import com.example.proyecto.model.MembershipSale;
import com.example.proyecto.service.MembershipSaleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/membership-sales")
@RequiredArgsConstructor
@Tag(name = "MembershipSales", description = "Provides methods for managing membership sales")
public class MembershipSaleController {
    private final MembershipSaleService membershipSaleService;

    @GetMapping
    @Operation(summary = "Get all membership sales")
    @ApiResponse(responseCode = "200", description = "List of all membership sales", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MembershipSale.class)))})
    public ResponseEntity<List<MembershipSaleResponse>> findAll() {
        List<MembershipSaleResponse> sales = membershipSaleService.findAll();
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get membership sale by ID")
    @ApiResponse(responseCode = "200", description = "Membership sale found", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MembershipSale.class)))})
    public ResponseEntity<MembershipSaleResponse> findById(@PathVariable Integer id) {
        MembershipSaleResponse sale = membershipSaleService.findById(id);
        return ResponseEntity.ok(sale);
    }

    @PostMapping
    @Operation(summary = "Create a new membership sale")
    @ApiResponse(responseCode = "200", description = "Membership sale created successfully",
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MembershipSale.class)))})
    public ResponseEntity<MembershipSaleResponse> create(@RequestBody MembershipSaleRequest saleRequest) {
        MembershipSaleResponse createdSale = membershipSaleService.save(saleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSale);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update membership sale by ID")
    @ApiResponse(responseCode = "200", description = "Membership sale updated successfully",
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MembershipSale.class)))})
    public ResponseEntity<MembershipSaleResponse> update(@PathVariable Integer id, @RequestBody MembershipSaleRequest saleRequest) {
        MembershipSaleResponse updatedSale = membershipSaleService.update(id, saleRequest);
        return ResponseEntity.ok(updatedSale);
    }

    // @DeleteMapping("/{id}")
    // @Operation(summary = "Delete membership sale by ID")
    // @ApiResponse(responseCode = "200", description = "Membership sale deleted successfully",
    //         content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MembershipSale.class)))})
    // public ResponseEntity<Void> delete(@PathVariable Integer id) {
    //     membershipSaleService.delete(id);
    //     return ResponseEntity.noContent().build();
    // }

    @GetMapping("/user/{idUser}")
    @Operation(summary = "Get membership sales by user ID")
    @ApiResponse(responseCode = "200", description = "List of membership sales for the specified user",
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MembershipSale.class)))})
    public List<MembershipSaleResponse> findByUserId(@PathVariable Integer idUser) {
        return membershipSaleService.findByUserId(idUser);
    }

    @GetMapping("/customer/{idCustomer}")
    @Operation(summary = "Get membership sales by customer ID")
    @ApiResponse(responseCode = "200", description = "List of membership sales for the specified customer",
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MembershipSale.class)))})
    public List<MembershipSaleResponse> findByCustomerId(@PathVariable Integer idCustomer) {
        return membershipSaleService.findByCustomerId(idCustomer);
    }

    @GetMapping("/gym/{idGym}")
    @Operation(summary = "Get membership sales by gym ID")
    @ApiResponse(responseCode = "200", description = "List of membership sales for the specified gym",
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MembershipSale.class)))})
    public List<MembershipSaleResponse> findByGymId(@PathVariable Integer idGym) {
        return membershipSaleService.findByGymId(idGym);
    }

    @GetMapping(value = "paginationAll", params = { "page", "pageSize" })
    @Operation(summary = "Get all membership sales pagination")
    public List<MembershipSaleResponse> getAllPaginated(@RequestParam(value = "page", defaultValue = "0", required = false) int page, 
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize){
        List<MembershipSaleResponse> membershipSales = membershipSaleService.getAll(page, pageSize);
        return membershipSales;
    }

    @GetMapping(value = "paginationByUserId", params = { "page", "pageSize" })
    @Operation(summary = "Get membership sales by user ID")
    public List<MembershipSaleResponse> getByUserId(@RequestParam(value = "page", defaultValue = "0", required = false) int page, 
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize, @RequestParam Integer idUser) {
        List<MembershipSaleResponse> membershipSales = membershipSaleService.findByUserId(page, pageSize, idUser);
        return membershipSales;
    }

    @GetMapping(value = "paginationByGymId", params = { "page", "pageSize" })
    @Operation(summary = "Get membership sales by gym ID")
    public List<MembershipSaleResponse> getByGymId(@RequestParam(value = "page", defaultValue = "0", required = false) int page, 
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize, @RequestParam Integer idGym) {
        List<MembershipSaleResponse> membershipSales = membershipSaleService.findByGymId(page, pageSize, idGym);
        return membershipSales;
    }
}