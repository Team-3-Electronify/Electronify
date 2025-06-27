package com.femcoders.electronify.review.dto;

public record ReviewResponse(Long id,
                             Double rating,
                             String body,
                             Long productId) {
}
