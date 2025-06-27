package com.femcoders.electronify.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ReviewRequest(@NotNull(message = "Rating is required") @PositiveOrZero(message = "Rating must be positive or zero")
                            @Max(value = 5, message = "Rating cannot be greater than 5") double rating,
                            @NotBlank(message = "Review text is required") String body,
                            @NotNull(message = "Product ID is required") Long productId,
                            @NotNull(message = "User ID is required") Long userId) {
}
