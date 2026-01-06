package com.back.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.back.app.model.SubscriptionPrice;

@Repository
public interface SubscriptionPriceRepo extends JpaRepository<SubscriptionPrice, Integer> {
    
}