package com.back.app.controller;

import com.back.app.service.SubscriptionPriceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Subscription Price", description = "Global configuration for the subscription cost (in cents).")
@RestController
@RequestMapping("/api/subscription-price")
@RequiredArgsConstructor
@Slf4j
public class SubscriptionPriceController {

    private final SubscriptionPriceService subscriptionPriceService;

    @Operation(summary = "Get current price", description = "Returns the subscription price in cents.")
    @GetMapping("/")
    public ResponseEntity<Long> getPrice() {
        return ResponseEntity.ok(subscriptionPriceService.getPrice());
    }

    @Operation(summary = "Update global price", description = "Sets a new global subscription price. Restricted to Admin.")
    @Secured("ROLE_ADMIN")
    @PostMapping("/set")
    public ResponseEntity<Long> setPrice(@RequestBody Long newPriceCents) {
        try {
            subscriptionPriceService.setPrice(newPriceCents);
            log.info("Subscription price updated to: {} cents", newPriceCents);
            return ResponseEntity.ok(newPriceCents);
        } catch (Exception e) {
            log.error("Error updating subscription price: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}