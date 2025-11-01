package com.back.app.controller;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.back.app.model.Advertisement;
import com.back.app.service.AdvertisementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Advertisements", description = "Operations related to advertisement listings, searching, and filtering.")
@RestController
@RequestMapping("/api/advertisements")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    @Operation(
        summary = "Retrieve all advertisements", 
        description = "Returns a comprehensive, unfiltered list of all active advertisement listings."
    )
    @GetMapping("/")
    public ResponseEntity<List<Advertisement>> getAllAdvertisements() {
        return ResponseEntity.ok().body(advertisementService.getAllAdvertisements());
    }

    @Operation(
        summary = "Retrieve advertisement by ID", 
        description = "Returns the details for a specific advertisement ID."
    )
    
    @GetMapping("/{id}")
    public ResponseEntity<Advertisement> getMethodName(@PathVariable Integer id) {
        return ResponseEntity.ok().body(advertisementService.getAdvertisementbyId(id));
    }

    @Operation(
        summary = "Search and Filter Advertisements", 
        description = "Returns advertisements matching the specified search criteria. All parameters are optional and combined using AND logic."
    )
    @GetMapping("/search")
    public ResponseEntity<List<Advertisement>> searchAdvertisements(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) LocalDate beginDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {

        log.info("Received search request - categoryId: {}, beginDate: {}, endDate: {}, minPrice: {}, maxPrice: {}",
                categoryId, beginDate, endDate, minPrice, maxPrice);

        List<Advertisement> advertisements = advertisementService.getFilteredAdvertisements(
                categoryId, beginDate, endDate, minPrice, maxPrice);

        return ResponseEntity.ok().body(advertisements);
    }
}