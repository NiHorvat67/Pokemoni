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

    /**
     * Creates a Stripe Checkout Session and returns the URL for the customer to pay.
     * * @param account The account associated with the payment.
     * @param amount The amount in cents (or smallest currency unit) to charge.
     * @return The URL of the Stripe Checkout page, or null if an error occurs.
     */
    public String createPaymentLink(Account account, long amount) {


        Stripe.apiKey = stripeApiKey;
        // log.info(stripeApiKey); // Be cautious logging keys
        log.info("Stripe API key is set.");
    
        // NOTE: It's best practice to retrieve these dynamically
        String CLIENT_BASE_URL = "http://localhost:8080"; 
        
        // Removed customerEmail assignment and setCustomerEmail call to prevent the Stripe Link prompt.
        // String customerEmail = "nikolahorvat2004@gmail.com"; 
        
        try {
            SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    // .setCustomerEmail(customerEmail) // Commented out to bypass Stripe Link login prompt
                    .setSuccessUrl(CLIENT_BASE_URL + "/success?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl(CLIENT_BASE_URL + "/failure");

            paramsBuilder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(
                                    PriceData.builder()
                                            .setProductData(
                                                    PriceData.ProductData.builder()
                                                            .putMetadata("app_id", "Trader_payment")
                                                            .setName("Trader Subscription")
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