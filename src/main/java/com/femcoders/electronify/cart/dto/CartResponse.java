package com.femcoders.electronify.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record CartResponse(@Schema(description = "cart ID", example = "1")
                           Long id,
                           @Schema(description = "Cart items list.")
                           Long userId,
                           @Schema()
                           List<CartItemDto> items,
                           @Schema(description = "Total price of cart.", example = "888.8")
                           double totalPrice) {
}
