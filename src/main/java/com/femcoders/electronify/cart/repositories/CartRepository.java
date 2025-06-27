package com.femcoders.electronify.cart.repositories;

import com.femcoders.electronify.cart.models.Cart;
import com.femcoders.electronify.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
