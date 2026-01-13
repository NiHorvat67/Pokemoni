package com.back.app.controller;

import com.back.app.service.StripeConnectService;
import com.stripe.exception.StripeException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Stripe Connect", description = "Operations related to Stripe Connect account onboarding and management")
@RestController
@RequestMapping("/api/stripe/connect")
@RequiredArgsConstructor
@Validated
@Slf4j
@CrossOrigin
public class StripeConnectController {

    private final StripeConnectService stripeConnectService;

    @Operation(summary = "Create onboarding link", description = "Creates or retrieves an onboarding link for a Stripe Connect account")
    @PostMapping("/onboard/{accountId}")
    public ResponseEntity<Map<String, String>> createOnboardingLink(@PathVariable Integer accountId) {
        try {
            log.info("Creating onboarding link for account ID: {}", accountId);
            String onboardingUrl = stripeConnectService.createAccountLink(accountId);
            
            Map<String, String> response = new HashMap<>();
            response.put("url", onboardingUrl);
            response.put("accountId", accountId.toString());
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Invalid request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (StripeException e) {
            log.error("Stripe API error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create onboarding link: " + e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unexpected error: " + e.getMessage()));
        }
    }

    @Operation(summary = "Refresh onboarding link", description = "Refreshes the onboarding link for a Stripe Connect account")
    @PostMapping("/refresh/{accountId}")
    public ResponseEntity<Map<String, String>> refreshOnboardingLink(@PathVariable Integer accountId) {
        try {
            log.info("Refreshing onboarding link for account ID: {}", accountId);
            String onboardingUrl = stripeConnectService.refreshAccountLink(accountId);
            
            Map<String, String> response = new HashMap<>();
            response.put("url", onboardingUrl);
            response.put("accountId", accountId.toString());
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Invalid request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (StripeException e) {
            log.error("Stripe API error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to refresh onboarding link: " + e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unexpected error: " + e.getMessage()));
        }
    }

    @Operation(summary = "Get account status", description = "Retrieves the status of a Stripe Connect account")
    @GetMapping("/status/{accountId}")
    public ResponseEntity<Map<String, String>> getAccountStatus(@PathVariable Integer accountId) {
        try {
            log.info("Getting status for account ID: {}", accountId);
            String status = stripeConnectService.getAccountStatus(accountId);
            String connectAccountId = stripeConnectService.getConnectAccountId(accountId);
            
            Map<String, String> response = new HashMap<>();
            response.put("status", status);
            response.put("accountId", accountId.toString());
            if (connectAccountId != null) {
                response.put("connectAccountId", connectAccountId);
            }
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Invalid request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (StripeException e) {
            log.error("Stripe API error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to get account status: " + e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unexpected error: " + e.getMessage()));
        }
    }
}
