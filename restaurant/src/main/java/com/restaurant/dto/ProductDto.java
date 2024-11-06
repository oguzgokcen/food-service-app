package com.restaurant.dto;

import com.restaurant.entity.Category;
import com.restaurant.entity.Restaurant;
import com.restaurant.entity.Tag;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
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
}
