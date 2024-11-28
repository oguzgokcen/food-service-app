package com.restaurant.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "orders",url = "${application.config.orders-url}")
public interface OrderClient {
    @GetMapping("/count/{productId}")
    Long getOrderCountByProductId(@PathVariable("productId") Long productId);
}
