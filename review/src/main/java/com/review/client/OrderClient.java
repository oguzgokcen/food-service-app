package com.review.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "orders", url = "http://localhost:8071/api/v1/orders")
public interface OrderClient {
    @GetMapping("/productIds")
    List<Long> getProductIds(@RequestParam("orderId") long orderId);

    @GetMapping("/isOrderPaid")
    boolean isOrderPaid(@RequestParam("orderId") long orderId);

    @GetMapping("/IdsByUserId")
    List<Long> getUsersOrderIds(@RequestParam("userId") long userId);
}