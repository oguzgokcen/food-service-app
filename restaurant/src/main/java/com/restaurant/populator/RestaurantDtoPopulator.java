package com.restaurant.populator;

import com.restaurant.dto.RestaurantDto;
import com.restaurant.entity.Restaurant;
import org.springframework.stereotype.Component;

@Component
public class RestaurantDtoPopulator extends AbstractPopulator<Restaurant, RestaurantDto> {
    @Override
    public RestaurantDto populate(Restaurant restaurant, RestaurantDto restaurantDto) {
        restaurantDto.setId(restaurant.getId());
        restaurantDto.setName(restaurant.getName());
        restaurantDto.setAddress(restaurant.getAddress());
        return restaurantDto;
    }

    @Override
    public RestaurantDto getTarget() {
        return new RestaurantDto();
    }

}
