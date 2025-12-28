package com.back.app.service;

import com.back.app.model.Account;
import com.back.app.model.StripeConnectAccount;
import com.back.app.repo.AccountRepo;
import com.back.app.repo.StripeConnectAccountRepo;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.AccountLink;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StripeConnectService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Value("${app.frontend.url}")
    private String clientBaseURL;

    private final AccountRepo accountRepo;
    private final StripeConnectAccountRepo stripeConnectAccountRepo;

    /**
     * Creates a Stripe Connect account for a trader
     * @param accountId The internal account ID
     * @return The Stripe Connect account ID
     */
    public String createConnectAccount(Integer accountId) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        Optional<Account> accountOpt = accountRepo.findById(accountId);
        if (accountOpt.isEmpty()) {
            throw new IllegalArgumentException("Account not found with ID: " + accountId);
        }

        Account appAccount = accountOpt.get();

        // Check if account already has a Stripe Connect account mapping
        Optional<StripeConnectAccount> existingMapping = stripeConnectAccountRepo.findByAccountId(accountId);
        if (existingMapping.isPresent()) {
            log.info("Account {} already has Stripe Connect account: {}", accountId, existingMapping.get().getStripeAccountId());
            return existingMapping.get().getStripeAccountId();
        }

        // Create Stripe Connect account
        AccountCreateParams params = AccountCreateParams.builder()
                .setType(AccountCreateParams.Type.EXPRESS)
                .setCountry("US") // You may want to make this configurable
                .setEmail(appAccount.getUserEmail())
                .setCapabilities(
                        AccountCreateParams.Capabilities.builder()
                                .setCardPayments(
                                        AccountCreateParams.Capabilities.CardPayments.builder()
                                                .setRequested(true)
                                                .build())
                                .setTransfers(
                                        AccountCreateParams.Capabilities.Transfers.builder()
                                                .setRequested(true)
                                                .build())
                                .build())
                .build();

        com.stripe.model.Account stripeAccount = com.stripe.model.Account.create(params);
        log.info("Created Stripe Connect account: {} for user: {}", stripeAccount.getId(), appAccount.getUserEmail());

        // Save mapping to Stripe Connect account table
        StripeConnectAccount mapping = new StripeConnectAccount();
        mapping.setAccountId(accountId);
        mapping.setStripeAccountId(stripeAccount.getId());
        mapping.setAccountStatus("pending");
        stripeConnectAccountRepo.save(mapping);

        return stripeAccount.getId();
    }

    /**
     * Creates an onboarding link for a Stripe Connect account
     * @param accountId The internal account ID
     * @return The onboarding URL
     */
    public String createAccountLink(Integer accountId) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        Optional<Account> accountOpt = accountRepo.findById(accountId);
        if (accountOpt.isEmpty()) {
            throw new IllegalArgumentException("Account not found with ID: " + accountId);
        }

        // Check if mapping exists, create account if it doesn't
        Optional<StripeConnectAccount> mappingOpt = stripeConnectAccountRepo.findByAccountId(accountId);
        String connectAccountId;
        if (mappingOpt.isEmpty()) {
            connectAccountId = createConnectAccount(accountId);
        } else {
            connectAccountId = mappingOpt.get().getStripeAccountId();
        }

        // Create account link for onboarding
        AccountLinkCreateParams params = AccountLinkCreateParams.builder()
                .setAccount(connectAccountId)
                .setRefreshUrl(clientBaseURL + "/stripe/connect/reauth")
                .setReturnUrl(clientBaseURL + "/stripe/connect/return")
                .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                .build();

        AccountLink accountLink = AccountLink.create(params);
        log.info("Created account link for Connect account: {}", connectAccountId);

        return accountLink.getUrl();
    }

    /**
     * Refreshes an account link (for re-authentication)
     * @param accountId The internal account ID
     * @return The refreshed onboarding URL
     */
    public String refreshAccountLink(Integer accountId) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        Optional<Account> accountOpt = accountRepo.findById(accountId);
        if (accountOpt.isEmpty()) {
            throw new IllegalArgumentException("Account not found with ID: " + accountId);
        }

        Optional<StripeConnectAccount> mappingOpt = stripeConnectAccountRepo.findByAccountId(accountId);
        if (mappingOpt.isEmpty()) {
            throw new IllegalStateException("No Stripe Connect account found for account ID: " + accountId);
        }

        String connectAccountId = mappingOpt.get().getStripeAccountId();

        AccountLinkCreateParams params = AccountLinkCreateParams.builder()
                .setAccount(connectAccountId)
                .setRefreshUrl(clientBaseURL + "/stripe/connect/reauth")
                .setReturnUrl(clientBaseURL + "/stripe/connect/return")
                .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                .build();

        AccountLink accountLink = AccountLink.create(params);
        log.info("Refreshed account link for Connect account: {}", connectAccountId);

        return accountLink.getUrl();
    }

    /**
     * Retrieves the status of a Stripe Connect account
     * @param accountId The internal account ID
     * @return The account status
     */
    public String getAccountStatus(Integer accountId) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        Optional<Account> accountOpt = accountRepo.findById(accountId);
        if (accountOpt.isEmpty()) {
            throw new IllegalArgumentException("Account not found with ID: " + accountId);
        }

        Optional<StripeConnectAccount> mappingOpt = stripeConnectAccountRepo.findByAccountId(accountId);
        if (mappingOpt.isEmpty()) {
            return "not_connected";
        }

        StripeConnectAccount mapping = mappingOpt.get();
        String connectAccountId = mapping.getStripeAccountId();

        // Retrieve account from Stripe to get latest status
        com.stripe.model.Account stripeAccount = com.stripe.model.Account.retrieve(connectAccountId);
        
        boolean chargesEnabled = stripeAccount.getChargesEnabled();
        boolean payoutsEnabled = stripeAccount.getPayoutsEnabled();
        boolean detailsSubmitted = stripeAccount.getDetailsSubmitted();

        String status;
        if (chargesEnabled && payoutsEnabled && detailsSubmitted) {
            status = "active";
        } else if (detailsSubmitted) {
            status = "pending";
        } else {
            status = "restricted";
        }

        // Update local status in mapping table
        mapping.setAccountStatus(status);
        stripeConnectAccountRepo.save(mapping);

        return status;
    }

    /**
     * Retrieves the Stripe Connect account ID for a given account
     * @param accountId The internal account ID
     * @return The Stripe Connect account ID, or null if not connected
     */
    public String getConnectAccountId(Integer accountId) {
        Optional<StripeConnectAccount> mappingOpt = stripeConnectAccountRepo.findByAccountId(accountId);
        if (mappingOpt.isEmpty()) {
            return null;
        }
        return mappingOpt.get().getStripeAccountId();
    }
}
