package com.femcoders.electronify.cart;

import com.femcoders.electronify.cart.dto.CartRequest;
import com.femcoders.electronify.cart.dto.CartResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Operations related to cart")
public class CartController {
    private final CartService cartService;

    @GetMapping
    @Operation(
            summary = "Get all products in a cart by user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cart by username returned",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CartResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cart for user {username}  does not exist.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<CartResponse> getCart() {
        return ResponseEntity.ok(cartService.getCartByUser());
    }

    @PostMapping("/add/{productId}")
    @Operation(
            summary = "Add new product in a user cart",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Product added to cart successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CartResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid quantity or bad request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<CartResponse> addToCart(
            @PathVariable Long productId,
            @RequestBody CartRequest request) {
        CartResponse cartResponse = cartService.addToCart(productId, request.quantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartResponse);
    }

    @PutMapping("/update/{productId}")
    @Operation(
            summary = "Update product quantity in cart by user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cart updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CartResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found in cart",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request data",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<CartResponse> updateCartItem(
            @PathVariable Long productId,
            @RequestBody CartRequest request) {
        CartResponse updatedCart = cartService.updateCartItemQuantity(productId, request.quantity());
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/remove/{productId}")
    @Operation(
            summary = "Delete product in cart by user",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Product successfully removed from cart",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found in cart",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<Void> removeFromCart(@PathVariable Long productId) {
        cartService.removeFromCart(productId);
        return ResponseEntity.noContent().build();
    }
}
