package com.femcoders.electronify.user.dto;

public record UserReviewResponse(
        Long id,
        Double rating,
        String body,
        Long productid) {
}
