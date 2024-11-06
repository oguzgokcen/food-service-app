package com.restaurant.service;

import com.restaurant.dto.ProductDto;
import com.restaurant.dto.request.ProductRequest;
import com.restaurant.entity.Category;
import com.restaurant.entity.Product;
import com.restaurant.entity.Restaurant;
import com.restaurant.entity.Tag;
import com.restaurant.exception.RestaurantNotFoundException;
import com.restaurant.model.CustomResponseMessages;
import com.restaurant.model.ResponseMessage;
import com.restaurant.populator.ProductDtoPopulator;
import com.restaurant.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final RestaurantService restaurantService;
    private final TagService tagService;
    private final ProductDtoPopulator productDtoPopulator;

    public ResponseEntity<ResponseMessage> addProduct(ProductRequest productRequest) throws RestaurantNotFoundException {
        Restaurant restaurant = restaurantService.findRestaurantById(productRequest.getRestaurantId());
        Optional<Category> category = restaurant.getCategories().stream().filter(e -> e.getId().equals(productRequest.getCategoryId())).findFirst();
        if (category.isEmpty())
            return ResponseEntity.internalServerError().body(new ResponseMessage(CustomResponseMessages.CATEGORY_DOES_NOT_EXISTS_IN_RESTAURANT, HttpStatus.INTERNAL_SERVER_ERROR));
        Set<Tag> tags = tagService.getTagsFromRequest(productRequest.getTags());
        Product product = Product.builder()
                .restaurant(restaurant)
                .category(category.get())
                .productName(productRequest.getProductName())
                .productShortDescription(productRequest.getProductShortDescription())
                .productDescription(productRequest.getProductDescription())
                .productPrice(productRequest.getProductPrice())
                .stock(productRequest.getStock())
                .tags(tags)
                .build();
        productRepository.save(product);

        return ResponseEntity.ok(new ResponseMessage(CustomResponseMessages.PRODUCT_CREATED, HttpStatus.OK));
    }

    public List<ProductDto> getAllProductsOfRestaurantId(Long restaurantId) {
        return productDtoPopulator.populateAll(productRepository.findProductsByRestaurantId(restaurantId));
    }

    public List<ProductDto> getAllProductsOfCategory(Long restaurantId, Long categoryId) {
        return productDtoPopulator.populateAll(productRepository.findProductsByRestaurantIdAndCategoryId(restaurantId, categoryId));
    }
}
