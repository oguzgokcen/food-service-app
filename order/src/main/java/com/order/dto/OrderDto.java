package com.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private Long restaurantId;
    private Long categoryId;
    private List<Long> productIdList;
    private Long totalPrice;
    private Long userId;
}