package com.back.app.repo;

import com.back.app.model.StripeConnectAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StripeConnectAccountRepo extends JpaRepository<StripeConnectAccount, Integer> {
    
    Optional<StripeConnectAccount> findByAccountId(Integer accountId);
    
    Optional<StripeConnectAccount> findByStripeAccountId(String stripeAccountId);
    
    boolean existsByAccountId(Integer accountId);
}
