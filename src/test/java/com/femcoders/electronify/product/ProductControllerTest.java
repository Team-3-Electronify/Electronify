package com.femcoders.electronify.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.femcoders.electronify.cloudinary.CloudinaryService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CloudinaryService cloudinaryService;

    @Test
    @Transactional
    void should_getAllProducts() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(4)))
                .andExpect(jsonPath("$[0].name").value("iPhone 15 Pro"))
                .andExpect(jsonPath("$[1].name").value("Samsung Galaxy S24 Ultra"))
                .andExpect(jsonPath("$[2].name").value("MacBook Pro 16"))
                .andExpect(jsonPath("$[3].name").value("Gaming Laptop ASUS ROG"));
    }

    @Test
    @Transactional
    void should_getProductById() throws Exception {
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("iPhone 15 Pro"))
                .andExpect(jsonPath("$.price").value(999.99))
                .andExpect(jsonPath("$.rating").value(4.5))
                .andExpect(jsonPath("$.reviewCount").value(2));
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void should_createNewProduct() throws Exception {
        when(cloudinaryService.uploadFile(any()))
                .thenReturn(java.util.Map.of("secure_url", "http://example.com/test-image.jpg"));

        MockMultipartFile mockImg = new MockMultipartFile(
                "image",
                "test-product.jpg",
                "image/jpeg",
                "fake-image-content".getBytes());

        mockMvc.perform(multipart("/api/products")
                        .file(mockImg)
                        .param("name", "Test Product")
                        .param("price", "299.99")
                        .param("featured", "true")
                        .param("categoryId", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.price").value(299.99))
                .andExpect(jsonPath("$.featured").value(true));
    }

    @Test
    @Transactional
    void should_getProductsByFilter() throws Exception {
        mockMvc.perform(get("/api/products/filter")
                        .param("categoryId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("iPhone 15 Pro"))
                .andExpect(jsonPath("$[1].name").value("Samsung Galaxy S24 Ultra"));
    }

    @Test
    @Transactional
    void should_getProductsByName() throws Exception {
        mockMvc.perform(get("/api/products/filter")
                        .param("name", "iPhone"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("iPhone 15 Pro"));
    }

    @Test
    @Transactional
    void should_getProductsByPriceRange() throws Exception {
        mockMvc.perform(get("/api/products/filter")
                        .param("priceGroup", "More than 900€"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(4)));
    }

    @Test
    @Transactional
    void should_sortProductsByPriceAsc() throws Exception {
        mockMvc.perform(get("/api/products/filter")
                        .param("sortByPrice", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(4)))
                .andExpect(jsonPath("$[0].name").value("iPhone 15 Pro"))
                .andExpect(jsonPath("$[3].name").value("MacBook Pro 16"));
    }

    @Test
    @Transactional
    void should_returnNotFound_whenProductNotExists() throws Exception {
        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound());
    }
}