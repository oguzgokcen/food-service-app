package com.restaurant.controller;

import com.restaurant.dto.RestaurantDto;
import com.restaurant.model.ResponseMessage;
import com.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping
    public List<RestaurantDto> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> saveRestaurant(@RequestBody RestaurantDto restaurantDto) {
        return restaurantService.saveRestaurant(restaurantDto);
    }


}
