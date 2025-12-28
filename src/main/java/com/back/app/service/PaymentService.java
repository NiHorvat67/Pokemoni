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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

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

  @Value("${app.backend.url}")
  String SERVER_BASE_URL;

  @Value("${stripe.connect.platform_fee_percent:10}")
  private Integer platformFeePercent; // Default 10% platform fee

  public String getStripeApiKey() {
    return stripeApiKey;
  }

  public String createPaymentLink(Account account, long amount) {

    Stripe.apiKey = stripeApiKey;
    log.info("Stripe API key is set.");

    try {
      SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
          .setMode(SessionCreateParams.Mode.PAYMENT)
          .setSuccessUrl(CLIENT_BASE_URL + "/auth/4")
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

  /**
   * Creates a checkout session for a reservation payment using Stripe Connect
   * @param connectAccountId The Stripe Connect account ID of the trader
   * @param amount Total amount in cents (price + deposit)
   * @param price Amount for the rental price in cents
   * @param deposit Amount for the deposit in cents
   * @param itemName Name of the item being rented
   * @param reservationId Reservation ID for metadata
   * @param advertisementId Advertisement ID for metadata
   * @return The checkout session URL
   */
  public String createConnectPaymentSession(
      String connectAccountId,
      long amount,
      long price,
      long deposit,
      String itemName,
      Integer reservationId,
      Integer advertisementId) throws StripeException {
    
    Stripe.apiKey = stripeApiKey;
    log.info("Creating Connect payment session for account: {}, amount: {}", connectAccountId, amount);

    try {
      // Calculate application fee (platform fee)
      long applicationFeeAmount = calculatePlatformFee(amount);

      // Build metadata
      Map<String, String> metadata = new HashMap<>();
      metadata.put("reservation_id", reservationId != null ? reservationId.toString() : "");
      metadata.put("advertisement_id", advertisementId != null ? advertisementId.toString() : "");
      metadata.put("payment_type", "reservation");

      SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
          .setMode(SessionCreateParams.Mode.PAYMENT)
          .setSuccessUrl(CLIENT_BASE_URL + "/reservations/success?session_id={CHECKOUT_SESSION_ID}")
          .setCancelUrl(CLIENT_BASE_URL + "/reservations/cancel")
          .putMetadata("reservation_id", reservationId != null ? reservationId.toString() : "")
          .putMetadata("advertisement_id", advertisementId != null ? advertisementId.toString() : "")
          .putMetadata("payment_type", "reservation");

      // Add payment intent data to specify connected account and application fee
      // For Express accounts with Direct Charges, use on_behalf_of and application_fee_amount
      paramsBuilder.setPaymentIntentData(
          SessionCreateParams.PaymentIntentData.builder()
              .setApplicationFeeAmount(applicationFeeAmount)
              .setOnBehalfOf(connectAccountId)
              .putMetadata("reservation_id", reservationId != null ? reservationId.toString() : "")
              .putMetadata("advertisement_id", advertisementId != null ? advertisementId.toString() : "")
              .build());

      // Add line items: rental price and deposit
      // Rental price
      paramsBuilder.addLineItem(
          SessionCreateParams.LineItem.builder()
              .setQuantity(1L)
              .setPriceData(
                  PriceData.builder()
                      .setProductData(
                          PriceData.ProductData.builder()
                              .setName(itemName != null ? itemName : "Rental Item")
                              .setDescription("Rental payment for " + (itemName != null ? itemName : "item"))
                              .build())
                      .setCurrency("usd")
                      .setUnitAmount(price)
                      .build())
              .build());

      // Deposit (only if deposit > 0)
      if (deposit > 0) {
        paramsBuilder.addLineItem(
            SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(
                    PriceData.builder()
                        .setProductData(
                            PriceData.ProductData.builder()
                                .setName("Security Deposit")
                                .setDescription("Refundable security deposit")
                                .build())
                        .setCurrency("usd")
                        .setUnitAmount(deposit)
                        .build())
                .build());
      }

      Session session = Session.create(paramsBuilder.build());
      log.info("Created Stripe Connect payment session: {} for account: {}", session.getId(), connectAccountId);

      return session.getUrl();

    } catch (StripeException e) {
      log.error("Stripe API error during Connect checkout session creation: {}", e.getMessage(), e);
      throw e;
    } catch (Exception e) {
      log.error("Unexpected error during Connect checkout session creation: {}", e.getMessage(), e);
      throw new RuntimeException("Failed to create payment session", e);
    }
  }

  /**
   * Creates a checkout session for a reservation payment using Stripe Connect (convenience method with BigDecimal)
   * @param connectAccountId The Stripe Connect account ID of the trader
   * @param price Rental price
   * @param deposit Security deposit
   * @param itemName Name of the item
   * @param reservationId Reservation ID
   * @param advertisementId Advertisement ID
   * @return The checkout session URL
   */
  public String createConnectPaymentSession(
      String connectAccountId,
      BigDecimal price,
      BigDecimal deposit,
      String itemName,
      Integer reservationId,
      Integer advertisementId) throws StripeException {
    
    long priceCents = convertToCents(price);
    long depositCents = convertToCents(deposit);
    long totalCents = priceCents + depositCents;

    return createConnectPaymentSession(
        connectAccountId,
        totalCents,
        priceCents,
        depositCents,
        itemName,
        reservationId,
        advertisementId);
  }

  /**
   * Calculates the platform fee amount
   * @param totalAmount Total payment amount in cents
   * @return Platform fee in cents
   */
  private long calculatePlatformFee(long totalAmount) {
    BigDecimal fee = BigDecimal.valueOf(totalAmount)
        .multiply(BigDecimal.valueOf(platformFeePercent))
        .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);
    return fee.longValue();
  }

  /**
   * Converts BigDecimal amount to cents (long)
   * @param amount Amount in dollars
   * @return Amount in cents
   */
  private long convertToCents(BigDecimal amount) {
    if (amount == null) {
      return 0L;
    }
    return amount.multiply(BigDecimal.valueOf(100)).longValue();
  }

}
