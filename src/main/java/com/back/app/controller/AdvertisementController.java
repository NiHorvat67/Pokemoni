package com.back.app.controller;

import java.util.HashMap;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.back.app.model.Account;

import com.back.app.model.AdverNoJoin;
import com.back.app.model.Advertisement;
import com.back.app.model.ItemType;
import com.back.app.service.AccountService;
import com.back.app.service.AdvertisementService;
import com.back.app.service.ItemTypeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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
    @Autowired
    private final AdvertisementService advertisementService;

    @Operation(summary = "Retrieve all advertisements", description = "Returns a comprehensive, unfiltered list of all active advertisement listings.")
    @GetMapping("/")
    public ResponseEntity<List<Advertisement>> getAllAdvertisements() {
        List<Advertisement> advertisements = advertisementService.getAllAdvertisements();
        advertisements.forEach((ad) -> advertisementService.hideSensitiveData(ad));
        return ResponseEntity.ok(advertisements);
    }

    @Operation(summary = "Retrieve advertisements by ID of trader", description = "")
    @GetMapping("/account/{id}")
    public ResponseEntity<List<Advertisement>> getAllAdvertisementsType(@PathVariable Integer id) {
        List<Advertisement> advertisements = advertisementService.getAllAdvertisementsByTrader(id);
        advertisements.forEach((ad) -> advertisementService.hideSensitiveData(ad));
        return ResponseEntity.ok(advertisements);
    }

    @Operation(summary = "Retrieve advertisement by ID", description = "Returns the details for a specific advertisement ID.")

    @GetMapping("/{id}")
    public ResponseEntity<Advertisement> getMethodName(@PathVariable Integer id) {
        Advertisement ad = advertisementService.getAdvertisementbyId(id);
        if (ad != null) {
            ad = advertisementService.hideSensitiveData(ad);
            return ResponseEntity.ok().body(ad);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Search and Filter Advertisements", description = "Returns advertisements matching the specified search criteria. All parameters are optional and combined using AND logic.")
    @GetMapping("/search")
    public ResponseEntity<List<Advertisement>> searchAdvertisements(
            @RequestParam(required = false) String itemName,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) LocalDate beginDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {

        log.info("Received search request - categoryId: {}, beginDate: {}, endDate: {}, minPrice: {}, maxPrice: {}",
                categoryId, beginDate, endDate, minPrice, maxPrice);

        List<Advertisement> advertisements = advertisementService.getFilteredAdvertisements(
                itemName, categoryId, beginDate, endDate, minPrice, maxPrice);

        return ResponseEntity.ok().body(advertisements);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createNewAdvertisement(@RequestBody String newAdvertisementString) {
        try {
            log.info("Received advertisement: {}", newAdvertisementString);

            AdverNoJoin advertisement = AdverNoJoin.convertToAdverNoJoin(newAdvertisementString);

            advertisementService.saveAdverNoJoin(advertisement);
            return ResponseEntity.ok().body(advertisement.getAdvertisementId().toString());

        } catch (JsonProcessingException e) {
            log.error("JSON parsing error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid JSON format: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)

                    .body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<String> editAdvertisement(@PathVariable Integer id,
            @RequestBody String newAdvertisementString) {
        try {
            log.info("Received advertisement: {}", newAdvertisementString);

            AdverNoJoin advertisement = AdverNoJoin.convertToAdverNoJoin(newAdvertisementString);

            advertisementService.updateAdverNoJoin(id, advertisement);
            return ResponseEntity.ok().body(advertisement.getAdvertisementId().toString());

        } catch (JsonProcessingException e) {
            log.error("JSON parsing error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid JSON format: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)

                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<String> deleteAdvertisement(@PathVariable Integer id) {
        try {
            advertisementService.deleteAccountById(id);
        } catch (Exception e) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting advertisement");
        }
        return ResponseEntity.ok().body("Succesfuly deleted advertisement with id " + id.toString());
    }

}
