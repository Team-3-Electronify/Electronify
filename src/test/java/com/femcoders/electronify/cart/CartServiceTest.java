package com.femcoders.electronify.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.femcoders.electronify.cart.dto.CartResponse;
import com.femcoders.electronify.cart.exeptions.CartNotFoundException;
import com.femcoders.electronify.cart.models.Cart;
import com.femcoders.electronify.cart.repositories.CartRepository;
import com.femcoders.electronify.product.ProductRepository;
import com.femcoders.electronify.user.UserRepository;
import com.femcoders.electronify.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;

import java.util.ArrayList;
import java.util.Optional;

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
        cartService = Mockito.spy(new CartService(userRepository, productRepository, cartRepository));
    }

    @Test
    void getCartByUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        Cart cart = new Cart(user);
        cart.setId(1L);
        cart.setItems(new ArrayList<>());
        cart.setTotalPrice(0.0);

        Mockito.doReturn(user).when(cartService).getAuthenticatedUser();
        Mockito.when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        CartResponse response = cartService.getCartByUser();

        assertNotNull(response);
        assertEquals(1L, response.userId());
        Mockito.verify(cartRepository).findByUser(user);
    }
}