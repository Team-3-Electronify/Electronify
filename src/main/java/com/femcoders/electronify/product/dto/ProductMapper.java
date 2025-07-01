package com.femcoders.electronify.product.dto;


import com.femcoders.electronify.category.Category;
import com.femcoders.electronify.category.dto.CategoryMapper;
import com.femcoders.electronify.product.Product;
import com.femcoders.electronify.review.dto.ReviewMapper;
import com.femcoders.electronify.review.dto.ReviewResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper {
    public static Product toEntity(ProductRequest productRequest ){
        Product product =  Product.builder()
                .name(productRequest.name())
                .price(productRequest.price())
                .imageUrl(productRequest.imageUrl())
                .featured(productRequest.featured())
                .build();

        return product;
    }

    public static ProductResponse fromEntity(Product product){
        List<ReviewResponse> reviews = product.getReviews()
                .stream()
                .map(review ->  ReviewMapper.toResponse(review))
                .toList();

        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), product.isFeatured(),
                CategoryMapper.fromEntity(product.getCategory()), product.getRating(), product.getReviewCount() , reviews);

    }
}
