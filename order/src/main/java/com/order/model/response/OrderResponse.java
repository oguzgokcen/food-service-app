package com.order.model.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long orderId;
    private RestaurantResponse restaurantResponse;
    private List<ProductResponse> productResponseList;
    private Long totalPrice;
    private UserResponse userResponse;
    private boolean isPaid;
}
