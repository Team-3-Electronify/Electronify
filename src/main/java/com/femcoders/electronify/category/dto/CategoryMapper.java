package com.femcoders.electronify.category.dto;

import com.femcoders.electronify.category.Category;

public class CategoryMapper {
    public static Category toEntity(CategoryRequest categoryRequest){
        Category category =  Category.builder()
                .name(categoryRequest.name())
                .build();
        return category;
    }

    public static CategoryResponse fromEntity(Category category){
        return new CategoryResponse(category.getId(), category.getName());
    }
}
