package com.restaurant.repository;

import com.restaurant.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("select p from  Product as p where p.restaurant.id = ?1")
    List<Product> findProductsByRestaurantId(Long restaurantId);

    @Query("select p from Product as p where p.restaurant.id = ?1 and p.category.id = ?2")
    List<Product> findProductsByRestaurantIdAndCategoryId(Long restaurantId, Long categoryId);
}
