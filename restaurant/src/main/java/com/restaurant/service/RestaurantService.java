package com.restaurant.service;

import com.restaurant.dto.CategoryDto;
import com.restaurant.dto.RestaurantDto;
import com.restaurant.dto.request.CategoryRequest;
import com.restaurant.entity.Category;
import com.restaurant.entity.Restaurant;
import com.restaurant.exception.CategoryNotFoundException;
import com.restaurant.exception.RestaurantNotFoundException;
import com.restaurant.model.CustomResponseMessages;
import com.restaurant.model.ResponseMessage;
import com.restaurant.populator.CategoryDtoPopulator;
import com.restaurant.populator.RestaurantDtoPopulator;
import com.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantDtoPopulator restaurantDtoPopulator;
    private final CategoryService categoryService;
    private final CategoryDtoPopulator categoryDtoPopulator;

    public List<RestaurantDto> getAllRestaurants() {
        return restaurantDtoPopulator.populateAll(restaurantRepository.findAll());
    }

    public ResponseEntity<ResponseMessage> saveRestaurant(RestaurantDto restaurantDto) {
        Restaurant restaurant = Restaurant.builder().name(restaurantDto.getName()).address(restaurantDto.getAddress()).build();
        restaurantRepository.save(restaurant);
        return ResponseEntity.ok(new ResponseMessage(CustomResponseMessages.RESTAURANT_CREATED, HttpStatus.OK));
    }

    public ResponseEntity<ResponseMessage> addNewCategoryToRestaurant(CategoryRequest categoryRequest) throws CategoryNotFoundException, RestaurantNotFoundException {
        Category category = categoryService.findCategory(categoryRequest.getCategoryName());
        Restaurant restaurant = restaurantRepository.findById(categoryRequest.getRestaurantId()).orElseThrow(()-> new RestaurantNotFoundException(CustomResponseMessages.RESTAURANT_NOT_FOUND));
        if(restaurant.getCategories().contains(category)) return ResponseEntity.internalServerError().body(new ResponseMessage(CustomResponseMessages.CATEGORY_ALREADY_EXISTS_IN_RESTAURANT,HttpStatus.INTERNAL_SERVER_ERROR));
        restaurant.getCategories().add(category);
        restaurantRepository.save(restaurant);
        return ResponseEntity.ok(new ResponseMessage(CustomResponseMessages.CATEGORY_ADDED_TO_RESTAURANT, HttpStatus.OK));
    }

    public Restaurant findRestaurantById(Long restaurantId) throws RestaurantNotFoundException {
        return restaurantRepository.findById(restaurantId).orElseThrow(()-> new RestaurantNotFoundException(CustomResponseMessages.RESTAURANT_NOT_FOUND));
    }

    public Set<CategoryDto> getCategoriesOfRestaurantByCategoryId(Long restaurantId) throws RestaurantNotFoundException {
        Restaurant restaurant= restaurantRepository.findById(restaurantId).orElseThrow(()->new RestaurantNotFoundException(CustomResponseMessages.RESTAURANT_NOT_FOUND));
        return categoryDtoPopulator.populateAllSet(restaurant.getCategories());
    }
}
