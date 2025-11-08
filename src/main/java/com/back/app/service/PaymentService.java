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

    @Value("${app.frontend.url}")
    String CLIENT_BASE_URL;

    public String createPaymentLink(Account account, long amount) {


        Stripe.apiKey = stripeApiKey;
        log.info("Stripe API key is set.");
        

        try {
            SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(CLIENT_BASE_URL + "/")
                    .setCancelUrl(CLIENT_BASE_URL + "/error?failed");

            paramsBuilder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(
                                    PriceData.builder()
                                            .setProductData(
                                                    PriceData.ProductData.builder()
                                                            .setName("Trader Subscription")
                                                            .setDescription("PAY ME " + "User")
                                                            .build())
                                            .setCurrency("usd")
                                            .setUnitAmount(Math.abs(amount)) 
                                            .build())
                            .build());

            Session session = Session.create(paramsBuilder.build());
            log.info("Stripe Session created with ID: {}", session.getId());
            
            // Return the session URL for redirection
            return session.getUrl();

        } catch (StripeException e) {
            log.error("Stripe API error during checkout session creation: {}", e.getMessage(), e);
            return null;
                    
        } catch (Exception e) {
            log.error("Unexpected error during checkout session creation: {}", e.getMessage(), e);
            return null;
        }

    }

}