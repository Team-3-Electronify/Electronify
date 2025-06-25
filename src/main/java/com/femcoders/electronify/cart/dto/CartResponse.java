package com.femcoders.electronify.cart.dto;

import java.util.List;

public record CartResponse(Long id, Long userId, List<CartItemDto> items, double totalPrice) {
}
