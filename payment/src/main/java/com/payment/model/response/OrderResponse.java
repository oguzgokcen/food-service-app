package com.payment.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long orderId;
    private RestaurantResponse restaurantResponse;
    private List<ProductResponse> productResponseList;
    private Long totalPrice;
    private UserResponse userResponse;
    private boolean isPaid;
}