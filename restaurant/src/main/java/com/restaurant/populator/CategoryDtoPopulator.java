package com.restaurant.populator;

import com.restaurant.dto.CategoryDto;
import com.restaurant.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryDtoPopulator extends AbstractPopulator<Category, CategoryDto> {
    @Override
    public CategoryDto populate(Category category, CategoryDto categoryDto) {
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }

    @Override
    public CategoryDto getTarget() {
        return new CategoryDto();
    }
}
