package com.order.client;

import com.order.model.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "products-service",url = "${application.config.products-url}")
public interface ProductClient {
    @PostMapping("/list")
    List<ProductResponse> getAllById(@RequestBody List<Long> productIdList);
}
