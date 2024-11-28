package com.order.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String productName;
    private String productShortDescription;
    private String productDescription;
    private Long productPrice;
    private String productImage;
    private CategoryResponse category;
    private Long stock;
}
