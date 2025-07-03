package com.femcoders.electronify.review.dto;

import com.femcoders.electronify.review.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {
    public static Review toEntity(ReviewRequest reviewRequest) {
        return Review.builder()
                .rating(reviewRequest.rating())
                .body(reviewRequest.body())
                .build();
    }

    public static ReviewResponse toResponse(Review review) {
        return new ReviewResponse(review.getId(), review.getRating(), review.getBody(), review.getProduct().getId(), review.getUser().getUsername());
    }
}