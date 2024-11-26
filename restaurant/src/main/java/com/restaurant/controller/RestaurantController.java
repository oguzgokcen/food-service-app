package com.restaurant.controller;

import com.restaurant.dto.CategoryDto;
import com.restaurant.dto.RestaurantDto;
import com.restaurant.dto.request.CategoryRequest;
import com.restaurant.exception.CategoryNotFoundException;
import com.restaurant.exception.RestaurantNotFoundException;
import com.restaurant.model.ResponseMessage;
import com.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping("/{restaurantId}")
    public RestaurantDto getRestaurantById(@PathVariable Long restaurantId) throws RestaurantNotFoundException {
        return restaurantService.getRestaurantById(restaurantId);
    }

    @GetMapping
    public List<RestaurantDto> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> saveRestaurant(@RequestBody RestaurantDto restaurantDto) {
        return restaurantService.saveRestaurant(restaurantDto);
    }

    @PutMapping("/category")
    public ResponseEntity<ResponseMessage> addNewCategoryToRestaurant(@RequestBody CategoryRequest categoryRequest) throws CategoryNotFoundException, RestaurantNotFoundException {
        return restaurantService.addNewCategoryToRestaurant(categoryRequest);
    }

    @GetMapping("/categories/{restaurantId}")
    public Set<CategoryDto> getCategoriesOfRestaurantByCategoryId(@PathVariable Long restaurantId) throws RestaurantNotFoundException {
        return restaurantService.getCategoriesOfRestaurantByCategoryId(restaurantId);
    }

    @GetMapping("/exists/{restaurantId}")
    public boolean existsByRestaurantId(@PathVariable Long restaurantId){
        return restaurantService.existsByRestaurantId(restaurantId);
    }

}
