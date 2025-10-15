package com.back.app.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/orders")
public class OrderRestController {

    private final OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<String> getOrders() {
        return orderService.getOrders();
    }

    @PostMapping
    public String addOrder(@RequestBody OrderService orderServiece) {
        orderService.addOrder(orderServiece.getOrderName());
        return "Order added: " + orderServiece.getOrderName();
    }
}
