package com.restaurant.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "reviews",url = "http://localhost:8073/api/v1/reviews")
public interface ReviewClient {
    @GetMapping("/rating")
    double getProductRating(@RequestParam("productId") Long productId);
}
