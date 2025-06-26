package com.femcoders.electronify.cart;

import com.femcoders.electronify.cart.dto.CartMapper;
import com.femcoders.electronify.cart.dto.CartResponse;
import com.femcoders.electronify.product.Product;
import com.femcoders.electronify.product.ProductRepository;
import com.femcoders.electronify.product.exceptions.NoIdProductFoundException;
import com.femcoders.electronify.user.UserRepository;
import com.femcoders.electronify.user.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    @Transactional
    public CartResponse addToCart(Long productId, int quantity) {
        User user = getAuthenticatedUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> new Cart(user));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoIdProductFoundException(productId));

        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem item = new CartItem(cart, product, quantity);
            cart.getItems().add(item);
        }
        calculateTotalPrice(cart);
        cartRepository.save(cart);

        return CartMapper.cartToResponse(cart);
    }

    private double calculateTotalPrice(Cart cart) {
        double total = cart.getItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
        cart.setTotalPrice(total);
        return total;
    }

    private User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
