package com.back.app.controller;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.back.app.model.Advertisement;
import com.back.app.service.AdvertisementService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/advertisement")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdvertisementController {
    
    private final AdvertisementService advertisementService;

    @GetMapping("/")
    public ResponseEntity<List<Advertisement>> getAllAdvertisements(){
        return ResponseEntity.ok().body(advertisementService.getAllAdvertisements());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Advertisement> getMethodName(@PathVariable Integer id) {
        return ResponseEntity.ok().body(advertisementService.getAdvertisementbyId(id));
    }
    

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