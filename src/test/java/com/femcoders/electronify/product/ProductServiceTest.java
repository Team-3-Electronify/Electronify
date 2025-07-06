package com.femcoders.electronify.product;

import com.femcoders.electronify.category.CategoryRepository;
import com.femcoders.electronify.cloudinary.CloudinaryService;
import com.femcoders.electronify.product.dto.ProductRequest;
import com.femcoders.electronify.product.dto.ProductResponse;
import com.femcoders.electronify.product.exceptions.NoIdProductFoundException;
import com.femcoders.electronify.product.exceptions.ProductAlreadyExistException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @MockitoBean
    private CloudinaryService cloudinaryService;

    @Test
    @Transactional
    void should_createNewProduct_fromRequest() throws Exception {
        MockMultipartFile mockImg = new MockMultipartFile(
                "image",
                "testphone.jpg",
                "image/jpeg",
                "fake-image-content".getBytes());

        Map<String, Object> fakeUrl = new HashMap<>();
        fakeUrl.put("secure_url", "https://res.cloudinary.com/demo/image/upload/testphone.jpg");
        when(cloudinaryService.uploadFile(mockImg)).thenReturn(fakeUrl);

        ProductRequest productRequest = new ProductRequest("Test Phone", 850.0, mockImg, true, 1L);
        ProductResponse actualResponse = productService.createNewProduct(productRequest);

        assertNotNull(actualResponse);
        assertEquals("Test Phone", actualResponse.name());
        assertEquals(850.0, actualResponse.price());
        assertEquals("https://res.cloudinary.com/demo/image/upload/testphone.jpg", actualResponse.imageUrl());
        assertTrue(actualResponse.featured());
        assertEquals("Smartphones & Accessories", actualResponse.category().name());
    }

    @Test
    @Transactional
    void should_throwException_when_productAlreadyExist() {
        MockMultipartFile mockImg = new MockMultipartFile(
                "image",
                "iphone15.jpg",
                "image/jpeg",
                "fake-image-content".getBytes());

        ProductRequest productRequest = new ProductRequest("iPhone 15 Pro", 850.0, mockImg, true, 1L);

        ProductAlreadyExistException exception = assertThrows(ProductAlreadyExistException.class, () -> productService.createNewProduct(productRequest));

        assertTrue(exception.getMessage().contains("iPhone 15 Pro"));
    }

    @Test
    @Transactional
    void should_findAllProducts() {
        List<ProductResponse> actualResponse = productService.findAllProducts();

        assertNotNull(actualResponse);
        assertEquals(4, actualResponse.size());
        assertEquals("iPhone 15 Pro", actualResponse.get(0).name());
        assertEquals("Samsung Galaxy S24 Ultra", actualResponse.get(1).name());
        assertEquals("MacBook Pro 16", actualResponse.get(2).name());
        assertEquals("Gaming Laptop ASUS ROG", actualResponse.get(3).name());
    }

    @Test
    @Transactional
    void should_findProductById() {
        ProductResponse actualResponse = productService.findProductById(1L);

        assertNotNull(actualResponse);
        assertEquals("iPhone 15 Pro", actualResponse.name());
        assertEquals(999.99, actualResponse.price());
        assertEquals(4.5, actualResponse.rating());
        assertEquals(2, actualResponse.reviewCount());
        assertEquals("Smartphones & Accessories", actualResponse.category().name());
    }

    @Test
    @Transactional
    void should_throwException_when_productNotFound() {
        assertThrows(NoIdProductFoundException.class, () -> productService.findProductById(999L));
    }

    @Test
    @Transactional
    void should_findProductsByFilters_byCategory() {
        List<ProductResponse> actualResponse = productService.findProductsByFilters(
                Optional.empty(), Optional.of(1L), Optional.empty(), Optional.empty(), Optional.empty());

        assertNotNull(actualResponse);
        assertEquals(2, actualResponse.size());
        assertEquals("iPhone 15 Pro", actualResponse.get(0).name());
        assertEquals("Samsung Galaxy S24 Ultra", actualResponse.get(1).name());
    }

    @Test
    @Transactional
    void should_findProductsByFilters_byName() {
        List<ProductResponse> actualResponse = productService.findProductsByFilters(
                Optional.of("iPhone"), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

        assertNotNull(actualResponse);
        assertEquals(1, actualResponse.size());
        assertEquals("iPhone 15 Pro", actualResponse.getFirst().name());
    }

    @Test
    @Transactional
    void should_findProductsByFilters_byPriceRange() {
        List<ProductResponse> actualResponse = productService.findProductsByFilters(
                Optional.empty(), Optional.empty(), Optional.of("More than 900€"), Optional.empty(), Optional.empty());

        assertNotNull(actualResponse);
        assertEquals(4, actualResponse.size());
    }

    @Test
    @Transactional
    void should_updateProduct() throws Exception {
        MockMultipartFile mockImg = new MockMultipartFile(
                "image",
                "updated-iphone.jpg",
                "image/jpeg",
                "fake-image-content".getBytes());

        Map<String, Object> fakeUrl = new HashMap<>();
        fakeUrl.put("secure_url", "https://res.cloudinary.com/demo/image/upload/updated-iphone.jpg");
        when(cloudinaryService.uploadFile(mockImg)).thenReturn(fakeUrl);

        ProductRequest updateRequest = new ProductRequest("Updated iPhone", 1099.99, mockImg, false, 1L);
        ProductResponse actualResponse = productService.updateProduct(1L, updateRequest);

        assertNotNull(actualResponse);
        assertEquals("updated iphone", actualResponse.name());
        assertEquals(1099.99, actualResponse.price());
        assertEquals("https://res.cloudinary.com/demo/image/upload/updated-iphone.jpg", actualResponse.imageUrl());
        assertFalse(actualResponse.featured());
    }

    @Test
    @Transactional
    void should_deleteProduct() {
        productService.deleteProductById(1L);

        assertThrows(NoIdProductFoundException.class, () -> productService.findProductById(1L));
    }

    @Test
    @Transactional
    void should_updateProductStats_when_reviewAdded() {
        productService.updateProductStats(1L);

        ProductResponse updatedProduct = productService.findProductById(1L);

        assertNotNull(updatedProduct);
        assertEquals(2, updatedProduct.reviewCount());
        assertEquals(4.5, updatedProduct.rating());
    }
}