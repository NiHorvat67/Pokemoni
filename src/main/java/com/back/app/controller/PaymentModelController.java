package com.back.app.controller;

import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.back.app.model.PaymentModel;
import com.back.app.service.PaymentModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Tag(name = "PaymentModelController", description = "Routes related to the recorded payments.")
@RestController
@RequestMapping("/api/payment-record")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PaymentModelController {
    
    private final PaymentModelService paymentModelService;

    @Operation(summary = "Retreive all the payment records")
    @GetMapping("/")
    public List<PaymentModel> getAllPaymentRecords(){

        return paymentModelService.getAllPaymentModel();

    }

    @Operation(summary = "Retreive payment-record by the paymentModel id")
    @GetMapping("/{id}")
    public PaymentModel getPaymentRecordById(@PathVariable Integer id){
        
        return paymentModelService.getPaymentModelById(id);

    }


}
