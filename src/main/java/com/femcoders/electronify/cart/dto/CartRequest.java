package com.femcoders.electronify.cart.dto;

import jakarta.validation.constraints.Min;

public record CartRequest(@Min(1) int quantity) {
}
