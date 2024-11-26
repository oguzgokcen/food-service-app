package com.order.controller;

import com.order.exception.OrderNotFoundException;
import com.order.model.request.OrderRequest;
import com.order.model.response.OrderResponse;
import com.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderApi {
    private final OrderService orderService;

    @GetMapping
    public String hello(@RequestHeader String userEmail){
        return "hello from" + userEmail;
    }


    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest,@RequestHeader String userEmail){
        return orderService.createOrder(orderRequest,userEmail);
    }

    @GetMapping("/order")
    public OrderResponse getOrderByOrderId(@RequestParam Long orderId,@RequestHeader String userEmail) throws OrderNotFoundException {
        return orderService.getOrderByOrderId(orderId,userEmail);
    }

    @GetMapping("/restaurant")
    public List<OrderResponse> getOrdersOfRestaurantById(@RequestParam Long restaurantId,@RequestHeader String userEmail){
        return orderService.getOrdersOfRestaurantById(restaurantId,userEmail);
    }

    @GetMapping("/count/{productId}")
    public Long getOrderCountByProductId(@PathVariable Long productId){
        return orderService.getOrderCountByProductId(productId);
    }



}