package com.femcoders.electronify.cart.dto;

import com.femcoders.electronify.cart.models.Cart;
import com.femcoders.electronify.cart.models.CartItem;

import java.util.List;
import java.util.stream.Collectors;

public class CartMapper {

    public static CartResponse cartToResponse(Cart cart) {
        List<CartItemDto> items = cart.getItems().stream()
                .map(CartMapper::mapToCartItemDto)
                .collect(Collectors.toList());

        return new CartResponse(
                cart.getId(),
                cart.getUser().getId(),
                items,
                cart.getTotalPrice()
        );
    }

    private static CartItemDto mapToCartItemDto(CartItem item) {
        return new CartItemDto(
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getProduct().getPrice(),
                item.getQuantity()
        );
    }
}