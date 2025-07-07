package com.femcoders.electronify.cart.dto;

public record CartItemDto(Long productId,
                               String name,
                               double price,
                               int quantity) {
}
