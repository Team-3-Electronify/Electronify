package com.femcoders.electronify.product.dto;

import com.femcoders.electronify.category.dto.CategoryResponse;
import com.femcoders.electronify.review.dto.ReviewResponse;

import java.util.List;

public record ProductResponse(
        Long id,
        String name,
        double price,
        String imageUrl,
        boolean featured,
        CategoryResponse category,
        double rating,
        int reviewCount,
        List<ReviewResponse> reviews
) {
}
