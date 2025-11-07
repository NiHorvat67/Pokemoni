package com.back.app.service;


import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.back.app.model.Account;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;

// receive payment in USD
@Service
@Validated
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    @Value("${stripe.api.key}") 
    private String stripeApiKey;

    public Session createPaymentLink(Account account, long amount) {


        Stripe.apiKey = stripeApiKey;
        log.info(stripeApiKey);
        log.info("stripe api key {}", Stripe.apiKey);
    
        String CLIENT_BASE_URL = "http://localhost:8080"; 
        String customerEmail = "nikolahorvat2004@gmail.com";

        try {
            SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setCustomerEmail(customerEmail)
                    .setSuccessUrl(CLIENT_BASE_URL + "/success?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl(CLIENT_BASE_URL + "/failure");

            paramsBuilder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(
                                    PriceData.builder()
                                            .setProductData(
                                                    PriceData.ProductData.builder()
                                                            .putMetadata("app_id", "penis_id")
                                                            .setName("penis")
                                                            .build())
                                            .setCurrency("usd")
                                            .setUnitAmount(Math.abs(amount)) 
                                            .build())
                            .build());

            Session session = Session.create(paramsBuilder.build());
            log.info("Stripe Session created: {}", session.getId());
            return session;

        } catch (StripeException e) {
            log.error("Stripe API error during checkout session creation: {}", e.getMessage(), e);
            return null;
                    
        } catch (Exception e) {
            log.error("Unexpected error during checkout session creation: {}", e.getMessage(), e);
            return null;
        }

    }

}
