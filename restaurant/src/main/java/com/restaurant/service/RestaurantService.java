package com.restaurant.service;

import com.restaurant.dto.RestaurantDto;
import com.restaurant.entity.Restaurant;
import com.restaurant.model.CustomResponseMessages;
import com.restaurant.model.ResponseMessage;
import com.restaurant.populator.RestaurantDtoPopulator;
import com.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantDtoPopulator restaurantDtoPopulator;

    public List<RestaurantDto> getAllRestaurants() {
        return restaurantDtoPopulator.populateAll(restaurantRepository.findAll());
    }

    public ResponseEntity<ResponseMessage> saveRestaurant(RestaurantDto restaurantDto) {
        Restaurant restaurant = Restaurant.builder().name(restaurantDto.getName()).address(restaurantDto.getAddress()).build();
        restaurantRepository.save(restaurant);
        return ResponseEntity.ok(new ResponseMessage(CustomResponseMessages.RESTAURANT_CREATED, HttpStatus.OK));
    }
}
