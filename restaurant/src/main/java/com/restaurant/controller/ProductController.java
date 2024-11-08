package com.restaurant.controller;

import com.restaurant.dto.ProductDto;
import com.restaurant.dto.request.ProductRequest;
import com.restaurant.exception.RestaurantNotFoundException;
import com.restaurant.model.ResponseMessage;
import com.restaurant.service.ProductService;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ResponseMessage> addProduct(@RequestBody ProductRequest product) throws RestaurantNotFoundException {
        return productService.addProduct(product);
    }

    @GetMapping("/{restaurantId}")
    public List<ProductDto> getAllProductsOfRestaurantId(@PathVariable Long restaurantId){
        return productService.getAllProductsOfRestaurantId(restaurantId);
    }

    @GetMapping("/{restaurantId}/{categoryId}")
    public List<ProductDto> getAllProductsOfCategory(@PathVariable Long restaurantId, @PathVariable Long categoryId){
        return productService.getAllProductsOfCategory(restaurantId,categoryId);
    }

    @PostMapping("/list")
    public List<ProductDto> getProductsFromIdList(@RequestBody List<Long> productIdList){
        return productService.getProductsFromIdList(productIdList);
    }
}
