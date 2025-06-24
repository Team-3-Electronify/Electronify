package com.femcoders.electronify.product.dto;

import com.femcoders.electronify.category.Category;
import com.femcoders.electronify.review.Review;

import java.util.List;

public record ProductResponse(
        Long id,
        String name,
        double price,
        String imageUrl,
        boolean featured,
        Category category,
        double rating,
        int reviewCount
        //,List<Review> reviews
) {
}
