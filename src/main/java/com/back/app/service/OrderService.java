
package com.back.app.service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private PaymentService paymentService;
    private final List<String> orders = new ArrayList<>();
    private String orderName;

    public List<String> getOrders() {
        return orders;
    }

    public void addOrder(String orderName) {
        orders.add(orderName);
        System.out.println("Order added: " + orderName);
    }

    // public OrderService(PaymentService paymentService) {
    // this.paymentService=paymentService;
    // }

    public void placeOrder() {
        this.paymentService.processPayment(30);
    }

    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }
}