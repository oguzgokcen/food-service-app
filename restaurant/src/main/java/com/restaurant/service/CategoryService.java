package com.restaurant.service;

import com.restaurant.entity.Category;
import com.restaurant.exception.CategoryNotFoundException;
import com.restaurant.model.CustomResponseMessages;
import com.restaurant.model.ResponseMessage;
import com.restaurant.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category findCategory(String categoryName) throws CategoryNotFoundException {
        return categoryRepository.findByName(categoryName).orElseThrow(() -> new CategoryNotFoundException(CustomResponseMessages.CATEGORY_NOT_FOUND));
    }

    public ResponseEntity<ResponseMessage> createCategory(String categoryName) {
        if (categoryRepository.findByName(categoryName).isEmpty())
            categoryRepository.save(Category.builder().name(categoryName).build());
        else
            return ResponseEntity.internalServerError().body(new ResponseMessage(CustomResponseMessages.CATEGORY_ALREADY_EXISTS, HttpStatus.INTERNAL_SERVER_ERROR));
        return ResponseEntity.ok(new ResponseMessage(CustomResponseMessages.CATEGORY_CREATED, HttpStatus.OK));
    }
}