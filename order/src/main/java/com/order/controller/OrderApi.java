package com.order.controller;

import com.order.model.request.OrderRequest;
import com.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderApi {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest){
        return orderService.createOrder(orderRequest);
    }

    @GetMapping
    public ResponseEntity<String> getOrder(@RequestBody OrderRequest orderRequest){
        return ResponseEntity.ok("AAA");
    }


}