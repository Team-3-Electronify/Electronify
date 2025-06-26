package com.femcoders.electronify.cart;

import com.femcoders.electronify.cart.dto.CartRequest;
import com.femcoders.electronify.cart.dto.CartResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/add/{productId}")
    public ResponseEntity<CartResponse> addToCart(
            @PathVariable Long productId,
            @RequestBody CartRequest request) {
        return ResponseEntity.ok(cartService.addToCart(productId, request.quantity()));
    }
}
