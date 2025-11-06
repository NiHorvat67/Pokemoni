package com.back.app.controller;

import com.back.app.service.AccountService;
import com.back.app.service.PaymentService;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@Validated
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final AccountService accountService;
    private final PaymentService paymentService;

    @Value("${app.frontend.url}")
    private String clientBaseURL;
    
    // for testing
    
    @PostMapping("/checkout/hosted")
    public ResponseEntity<String> hostedCheckout() throws StripeException {
        try{
            Session session = paymentService.createPaymentLink(null, 100);


            log.info("Stripe Session created: {}", session.getId());
            
            return ResponseEntity.ok(session.getUrl());

        } catch (Exception e) {
            log.error("Unexpected error during checkout session creation: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }
}
