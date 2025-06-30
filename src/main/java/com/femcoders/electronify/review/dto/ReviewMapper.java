package com.femcoders.electronify.review.dto;

import com.femcoders.electronify.product.Product;
import com.femcoders.electronify.review.Review;

public class ReviewMapper {
    public Review toEntity(ReviewRequest reviewRequest) {
        Review review = Review.builder()
                .rating(reviewRequest.rating())
                .body(reviewRequest.body())
                .build();
        return review;
    }

    public ReviewResponse toResponse(Review review) {
        return new ReviewResponse(review.getId(), review.getRating(), review.getBody(), review.getProduct().getId());
    }
}
