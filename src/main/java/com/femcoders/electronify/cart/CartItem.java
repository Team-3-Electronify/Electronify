package com.femcoders.electronify.cart;

import com.femcoders.electronify.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

@Entity
@Table(name="cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name="product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name="cart_id", nullable = false)
    private Cart cart;

    @Min(1)
    private int quantity;
}
