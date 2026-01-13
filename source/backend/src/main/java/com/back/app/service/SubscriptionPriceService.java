package com.back.app.service;

import com.back.app.model.SubscriptionPrice;
import com.back.app.repo.SubscriptionPriceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SubscriptionPriceService {

    private final SubscriptionPriceRepo subscriptionPriceRepo;

    public Long getPrice() {
        return subscriptionPriceRepo.findAll().stream()
                .findFirst()
                .map(SubscriptionPrice::getPrice)
                .orElseThrow(() -> new RuntimeException("Subscription price not found in database"));
    }

    @Transactional
    public SubscriptionPrice setPrice(Long newPriceCents) {
        Objects.requireNonNull(newPriceCents, "Price cannot be null");

        subscriptionPriceRepo.deleteAll();

        SubscriptionPrice subscriptionPrice = new SubscriptionPrice();
        subscriptionPrice.setPrice(newPriceCents);

        return subscriptionPriceRepo.save(subscriptionPrice);
    }
}