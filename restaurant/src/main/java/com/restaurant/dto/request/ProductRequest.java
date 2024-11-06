package com.restaurant.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private Long restaurantId;
    private Long categoryId;
    private String productName;
    private String productShortDescription;
    private String productDescription;
    private Long productPrice;
    private Long stock;
    private Set<String> tags;
}
