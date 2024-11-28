package com.restaurant.populator;

import com.restaurant.client.OrderClient;
import com.restaurant.client.ReviewClient;
import com.restaurant.dto.ProductDto;
import com.restaurant.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductDtoPopulator extends AbstractPopulator<Product, ProductDto> {
    private final TagDtoPopulator tagDtoPopulator;
    private final CategoryDtoPopulator categoryDtoPopulator;
    private final OrderClient orderClient;
    private final ReviewClient reviewClient;

    @Override
    public ProductDto populate(Product product, ProductDto productDto) {
        productDto.setId(product.getId());
        productDto.setProductName(product.getProductName());
        productDto.setProductShortDescription(product.getProductShortDescription());
        productDto.setProductDescription(product.getProductDescription());
        productDto.setProductPrice(product.getProductPrice());
        productDto.setProductImage(product.getProductImage());
        productDto.setTags(tagDtoPopulator.populateAllSet(product.getTags()));
        productDto.setCategory(categoryDtoPopulator.populate(product.getCategory()));
        productDto.setOrderCount(orderClient.getOrderCountByProductId(product.getId()));
        productDto.setProductRating(reviewClient.getProductRating(product.getId()));
        productDto.setStock(product.getStock());
        return productDto;
    }

    @Override
    public ProductDto getTarget() {
        return new ProductDto();
    }
}
