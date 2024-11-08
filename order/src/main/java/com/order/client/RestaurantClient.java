package com.order.client;

import com.order.model.response.RestaurantResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "restaurants-service",url = "${application.config.restaurants-url}") //ilgili kısımla http bağlantısı kurar
public interface RestaurantClient {
    @GetMapping("/{restaurantId}")
    RestaurantResponse getById(@PathVariable Long restaurantId);
}
