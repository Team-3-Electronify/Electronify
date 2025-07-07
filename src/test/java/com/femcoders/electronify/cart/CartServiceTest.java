package com.femcoders.electronify.cart;

import com.femcoders.electronify.cart.dto.CartResponse;
import com.femcoders.electronify.cart.exeptions.CartNotFoundException;
import com.femcoders.electronify.cart.exeptions.ItemNotFoundException;
import com.femcoders.electronify.cart.models.Cart;
import com.femcoders.electronify.cart.models.CartItem;
import com.femcoders.electronify.cart.repositories.CartRepository;
import com.femcoders.electronify.category.Category;
import com.femcoders.electronify.product.Product;
import com.femcoders.electronify.product.ProductRepository;
import com.femcoders.electronify.product.exceptions.NoIdProductFoundException;
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

import java.util.ArrayList;
import java.util.List;
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

    @InjectMocks
    private CartService cartService;

    private User user;
    private Cart cart;
    private Product product;
    private CartItem item;

    @BeforeEach
    public void setUp() {
        cartService = Mockito.spy(new CartService(userRepository, productRepository, cartRepository));

        user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        cart = new Cart(user);
        cart.setId(1L);
        cart.setItems(new ArrayList<>());
        cart.setTotalPrice(0.0);

        product = Product.builder()
                .id(100L)
                .name("Test Product")
                .price(10.0)
                .featured(false)
                .imageUrl("img.jpg")
                .category(new Category())
                .build();
    }

    @Test
    void getCartByUser() {
        Mockito.doReturn(user).when(cartService).getAuthenticatedUser();
        Mockito.when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        CartResponse response = cartService.getCartByUser();

        assertNotNull(response);
        assertEquals(1L, response.userId());
        Mockito.verify(cartRepository).findByUser(user);
    }

    @Test
    void getCartByUser_WhenCartNotFound() {
        user.setUsername("noCartUser");

        Mockito.doReturn(user).when(cartService).getAuthenticatedUser();
        Mockito.when(cartRepository.findByUser(user)).thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class,
                () -> cartService.getCartByUser());
    }

    @Test
    void addToCart_shouldAddNewItemToCart() {
        Mockito.doReturn(user).when(cartService).getAuthenticatedUser();
        Mockito.when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        Mockito.when(productRepository.findById(100L)).thenReturn(Optional.of(product));

        CartResponse response = cartService.addToCart(100L, 2);

        assertEquals(1, response.items().size());
        assertEquals(20.0, response.totalPrice(), 0.01);
        Mockito.verify(cartRepository).save(Mockito.any(Cart.class));
    }

    @Test
    void addToCart_WhenProductNotFound() {
        Mockito.doReturn(user).when(cartService).getAuthenticatedUser();
        Mockito.when(cartRepository.findByUser(user)).thenReturn(Optional.of(new Cart(user)));
        Mockito.when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NoIdProductFoundException.class,
                () -> cartService.addToCart(999L, 1));
    }

    @Test
    void updateCartItemQuantity_shouldUpdate() {
        item = new CartItem(cart, product, 1);
        cart.setItems(new ArrayList<>(List.of(item)));

        Mockito.doReturn(user).when(cartService).getAuthenticatedUser();
        Mockito.when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        Mockito.when(productRepository.findById(100L)).thenReturn(Optional.of(product));

        CartResponse response = cartService.updateCartItemQuantity(100L, 3);

        assertEquals(3, response.items().get(0).quantity());
        Mockito.verify(cartRepository).save(Mockito.any(Cart.class));
    }

    @Test
    void updateCartItemQuantity_WhenItemNotFound() {
        Mockito.doReturn(user).when(cartService).getAuthenticatedUser();
        Mockito.when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        Mockito.when(productRepository.findById(100L)).thenReturn(Optional.of(product));

        assertThrows(ItemNotFoundException.class,
                () -> cartService.updateCartItemQuantity(100L, 2));
    }

    @Test
    void removeFromCart() {
        item = new CartItem(cart, product, 1);
        cart.setItems(new ArrayList<>(List.of(item)));

        Mockito.doReturn(user).when(cartService).getAuthenticatedUser();
        Mockito.when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        cartService.removeFromCart(100L);

        assertTrue(cart.getItems().isEmpty());
        Mockito.verify(cartRepository).save(cart);
    }
}