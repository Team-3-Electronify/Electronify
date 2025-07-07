package com.femcoders.electronify.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.femcoders.electronify.cart.dto.CartItemDto;
import com.femcoders.electronify.cart.dto.CartRequest;
import com.femcoders.electronify.cart.dto.CartResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
@AutoConfigureMockMvc(addFilters = false)
class CartControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    private CartResponse cartResponse;
    private final Long productId = 1L;
    CartItemDto item;
    CartRequest request;

    @BeforeEach
    void setUp() {
        request = new CartRequest(2);
        item = new CartItemDto(productId, "Test Product", 10.0, 2);
        cartResponse = new CartResponse(1L, 1L, List.of(item), 20.0);
    }

    @Test
    void getCartTest() throws Exception {
        Mockito.when(cartService.getCartByUser()).thenReturn(cartResponse);

        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.items[0].productId").value(productId));
    }

    @Test
    void addToCartTest() throws Exception {
        Mockito.when(cartService.addToCart(productId, 2)).thenReturn(cartResponse);

        mockMvc.perform(post("/api/cart/add/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.items[0].quantity").value(2));
    }

    @Test
    void updateCartItem() throws Exception {
        CartResponse updated = new CartResponse(1L, 1L, List.of(item), 10.0);

        Mockito.when(cartService.updateCartItemQuantity(productId, 2)).thenReturn(updated);

        mockMvc.perform(put("/api/cart/update/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].quantity").value(2))
                .andExpect(jsonPath("$.totalPrice").value(10.0));
    }

    @Test
    void removeFromCartTest() throws  Exception {
        mockMvc.perform(delete("/api/cart/remove/{productId}", productId))
                .andExpect(status().isNoContent());

        Mockito.verify(cartService).removeFromCart(productId);
    }
}