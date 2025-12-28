package com.back.app.controller;

import com.back.app.model.Advertisement;
import com.back.app.model.Reservation;
import com.back.app.service.AccountService;
import com.back.app.service.AdvertisementService;
import com.back.app.service.PaymentService;
import com.back.app.service.ReservationService;
import com.back.app.service.StripeConnectService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin
@Validated
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final AccountService accountService;
    private final PaymentService paymentService;
    private final ReservationService reservationService;
    private final AdvertisementService advertisementService;
    private final StripeConnectService stripeConnectService;

    @Value("${app.frontend.url}")
    private String clientBaseURL;
    
    // for testing
    
    @GetMapping("/trader/checkout")
    public String hostedCheckout() throws StripeException {
        try{
            String link = paymentService.createPaymentLink(null, 100);

            log.info("Stripe Session created: {}", link);
            return "redirect:" + link;

        } catch (Exception e) {
            log.error("Unexpected error during checkout session creation: {}", e.getMessage(), e);
            return "server error";
        }
    }

    @Operation(summary = "Initiate payment for reservation", description = "Creates a Stripe Connect payment session for a reservation")
    @PostMapping("/reservation/{reservationId}")
    public ResponseEntity<Map<String, String>> initiateReservationPayment(@PathVariable Integer reservationId) {
        try {
            log.info("Initiating payment for reservation ID: {}", reservationId);

            // Get reservation
            Reservation reservation = reservationService.getReservationbyId(reservationId);
            if (reservation == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Reservation not found"));
            }

            // Get advertisement
            Advertisement advertisement = advertisementService.getAdvertisementbyId(reservation.getAdvertisementId());
            if (advertisement == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Advertisement not found"));
            }

            // Check if trader exists and has Stripe Connect account
            if (advertisement.getTrader() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Advertisement has no associated trader"));
            }

            Integer traderId = advertisement.getTrader().getAccountId();
            String connectAccountId = stripeConnectService.getConnectAccountId(traderId);

            if (connectAccountId == null || connectAccountId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Trader has not set up Stripe Connect account. Please complete onboarding first."));
            }

            // Check account status
            String accountStatus = stripeConnectService.getAccountStatus(traderId);
            if (!"active".equals(accountStatus)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Trader's Stripe Connect account is not active. Status: " + accountStatus));
            }

            // Create payment session
            String paymentUrl = paymentService.createConnectPaymentSession(
                    connectAccountId,
                    advertisement.getAdvertisementPrice(),
                    advertisement.getAdvertisementDeposit(),
                    advertisement.getItemName(),
                    reservationId,
                    advertisement.getAdvertisementId());

            Map<String, String> response = new HashMap<>();
            response.put("payment_url", paymentUrl);
            response.put("reservation_id", reservationId.toString());
            response.put("advertisement_id", advertisement.getAdvertisementId().toString());

            return ResponseEntity.ok(response);

        } catch (StripeException e) {
            log.error("Stripe API error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create payment session: " + e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unexpected error: " + e.getMessage()));
        }
    }

    @Operation(summary = "Verify payment success", description = "Verifies that a payment session was completed successfully. Called after Stripe redirects to success URL.")
    @GetMapping("/success")
    public ResponseEntity<Map<String, Object>> verifyPaymentSuccess(@RequestParam String session_id) {
        try {
            log.info("Verifying payment success for session: {}", session_id);

            // Initialize Stripe with API key
            Stripe.apiKey = paymentService.getStripeApiKey();
            
            // Retrieve the session from Stripe
            Session session = Session.retrieve(session_id);
            
            // Check if payment was successful
            // Payment status can be: "paid", "unpaid", "no_payment_required"
            String paymentStatus = session.getPaymentStatus();
            if ("paid".equals(paymentStatus)) {
                // Extract reservation ID from metadata
                String reservationIdStr = session.getMetadata().get("reservation_id");
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("session_id", session_id);
                response.put("payment_status", "paid");
                
                if (reservationIdStr != null && !reservationIdStr.isEmpty()) {
                    try {
                        Integer reservationId = Integer.parseInt(reservationIdStr);
                        response.put("reservation_id", reservationId);
                        
                        // Optionally update reservation status here if needed
                        // For now, we just confirm the payment was successful
                        log.info("Payment confirmed for reservation ID: {}", reservationId);
                    } catch (NumberFormatException e) {
                        log.error("Invalid reservation ID in session metadata: {}", reservationIdStr);
                    }
                }
                
                return ResponseEntity.ok(response);
            } else {
                // Payment not completed
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("session_id", session_id);
                response.put("payment_status", paymentStatus != null ? paymentStatus : "unknown");
                response.put("message", "Payment not completed");
                
                return ResponseEntity.ok(response);
            }
            
        } catch (StripeException e) {
            log.error("Stripe API error verifying payment: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", "Failed to verify payment: " + e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error verifying payment: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", "Unexpected error: " + e.getMessage()));
        }
    }
}
