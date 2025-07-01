package com.femcoders.electronify.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.femcoders.electronify.cart.repositories.CartRepository;
import com.femcoders.electronify.product.ProductRepository;
import com.femcoders.electronify.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;


    @InjectMocks
    private CartService cartService;

    @BeforeEach
    public void setUp() {
        this.cartService = new CartService(userRepository,productRepository,cartRepository);
    }

    @Test
    void getCartByUser() {

    }

    @Test
    void removeFromCart() {
    }
}