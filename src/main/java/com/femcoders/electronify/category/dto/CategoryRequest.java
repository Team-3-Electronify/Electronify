package com.femcoders.electronify.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 30, message = "Name must contain min 2 and max 30 characters")
        String name) {
}
