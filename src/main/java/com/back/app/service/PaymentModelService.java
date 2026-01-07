package com.back.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.back.app.repo.PaymentModelRepo;
import com.back.app.model.PaymentModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentModelService {
    

    private final PaymentModelRepo paymentModelRepo;



    public List<PaymentModel> getAllPaymentModel(){
        return paymentModelRepo.findAll();
    }


    public PaymentModel getPaymentModelById(Integer id){

        Optional<PaymentModel> optionalPaymentModel = paymentModelRepo.findById(id);
        if (optionalPaymentModel.isPresent()) {
            return optionalPaymentModel.get();
        }
        log.info("Employee with id: {} doesn't exist", id);
        return null;
    }

    


}
