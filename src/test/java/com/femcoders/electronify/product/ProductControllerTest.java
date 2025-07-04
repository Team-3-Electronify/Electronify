package com.femcoders.electronify.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.femcoders.electronify.category.dto.CategoryResponse;
import com.femcoders.electronify.cloudinary.CloudinaryService;
import com.femcoders.electronify.product.dto.ProductRequest;
import com.femcoders.electronify.product.dto.ProductResponse;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private CloudinaryService cloudinaryService;

    @Test
    void should_getAllProducts() throws Exception {
        mockMvc.perform(get("/api/products").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(15)))
                .andExpect(jsonPath("$[0].name").value("iPhone 15 Pro"))
                .andExpect(jsonPath("$[1].price").value("1299.99"));
    }

    @Test
    void should_getProductById() throws Exception {
        mockMvc.perform(get("/api/products/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("iPhone 15 Pro"))
                .andExpect(jsonPath("$.price").value("999.99"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void should_createNewProduct() throws Exception {
        ProductResponse fakeResponse = new ProductResponse(
                16L,
                "iPhone 16",
                1299.99,
                "https://example.com/fake-image.jpg",
                true,
                new CategoryResponse(1L, "Smartphones & Accessories"),
                0.0,
                0,
                List.of()
        );

        Mockito.when(productService.createNewProduct(Mockito.any()))
                .thenReturn(fakeResponse);

        MockMultipartFile mockImg = new MockMultipartFile(
                "image",
                "iphone16.jpg",
                "image/jpeg",
                "fake-image-content".getBytes()
        );

        mockMvc.perform(multipart("/api/products")
                        .file(mockImg)
                        .param("name", "iPhone 16")
                        .param("price", "1299.99")
                        .param("featured", "true")
                        .param("categoryId", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("iPhone 16"));
    }

    @Test
    void should_getProductsByFilter () throws Exception{
        mockMvc.perform(get("/api/products/filter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());

    }

}
