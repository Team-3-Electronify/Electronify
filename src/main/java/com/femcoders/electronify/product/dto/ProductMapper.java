package com.femcoders.electronify.product.dto;


import com.femcoders.electronify.category.Category;
import com.femcoders.electronify.category.dto.CategoryMapper;
import com.femcoders.electronify.exceptions.EmptyListException;
import com.femcoders.electronify.product.Product;
import com.femcoders.electronify.review.dto.ReviewMapper;
import com.femcoders.electronify.review.dto.ReviewResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ProductMapper {
    public static Product toEntity(ProductRequest productRequest, String imageUrl, Category category ){
        Product product =  Product.builder()
                .name(productRequest.name())
                .price(productRequest.price())
                .imageUrl(imageUrl)
                .featured(productRequest.featured())
                .category(category)
                .build();

        return product;
    }

    public static ProductResponse fromEntity(Product product){
        List<ReviewResponse> reviews = Optional.ofNullable(product.getReviews())
                .orElseGet(ArrayList::new)
                .stream()
                .map(review -> ReviewMapper.toResponse(review))
                .toList();

        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), product.isFeatured(),
                CategoryMapper.fromEntity(product.getCategory()), product.getRating(), product.getReviewCount() , reviews);

    }
}
