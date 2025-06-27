package com.femcoders.electronify.cart.repositories;

import com.femcoders.electronify.cart.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
