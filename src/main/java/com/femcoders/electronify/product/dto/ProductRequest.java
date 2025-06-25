package com.femcoders.electronify.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record ProductRequest(
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 50, message = "Name must contain min 2 and max 50 characters")
        String name,

        @Positive(message = "The number entered must be positive")
        double price,

        @NotBlank(message = "The url image is required")
        @URL(message = "The URL is not valid")
        String imageUrl,

        boolean featured,

        @NotNull(message = "Category ID cannot be null")
        @Positive(message = "The category ID cannot be less than 1")
        Long categoryId
) {
}
