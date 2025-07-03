package com.femcoders.electronify.category.dto;

import com.femcoders.electronify.category.Category;
import com.femcoders.electronify.product.dto.ProductMapper;
import com.femcoders.electronify.product.dto.ProductResponse;

import java.util.List;

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

    public static CategoryWithProductsResponse toCategoryWithProducts(Category category){
        List<ProductResponse> products = category.getProducts()
                .stream()
                .map(product ->  ProductMapper.fromEntity(product))
                .toList();
        return new CategoryWithProductsResponse(category.getId(), category.getName(), products);
    }
}
