package com.femcoders.electronify.cart;

import com.femcoders.electronify.cart.dto.CartRequest;
import com.femcoders.electronify.cart.dto.CartResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Operations related to cart")
public class CartController {
    private final CartService cartService;

    @GetMapping
    @Operation(summary = "Get all products in a cart by user")
    public ResponseEntity<CartResponse> getCart() {
        return ResponseEntity.ok(cartService.getCartByUser());
    }

    @PostMapping("/add/{productId}")
    @Operation(summary = "Add new product in a user cart")
    public ResponseEntity<CartResponse> addToCart(
            @PathVariable Long productId,
            @RequestBody CartRequest request) {
        CartResponse cartResponse = cartService.addToCart(productId, request.quantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartResponse);
    }

    @PutMapping("/update/{productId}")
    @Operation(summary = "Update product in cart by user")
    public ResponseEntity<CartResponse> updateCartItem(
            @PathVariable Long productId,
            @RequestBody CartRequest request) {
        CartResponse updatedCart = cartService.updateCartItemQuantity(productId, request.quantity());
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/remove/{productId}")
    @Operation(summary = "Delete product in cart by user")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long productId) {
        cartService.removeFromCart(productId);
        return ResponseEntity.ok().build();
    }
}
