package com.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String productName;
    private String productShortDescription;
    private String productDescription;
    private Long productPrice;
    private String productImage;
    private Set<TagDto> tags;
    private CategoryDto category;
    private Long orderCount;
}
