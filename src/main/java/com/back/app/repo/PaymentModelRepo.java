package com.back.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.back.app.model.PaymentModel;

public interface PaymentModelRepo extends JpaRepository<PaymentModel, Integer>{
    
}
