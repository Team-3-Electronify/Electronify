package com.femcoders.electronify.product;

import com.femcoders.electronify.category.Category;
import com.femcoders.electronify.category.CategoryRepository;
import com.femcoders.electronify.category.dto.CategoryMapper;
import com.femcoders.electronify.cloudinary.CloudinaryService;
import com.femcoders.electronify.product.dto.ProductMapper;
import com.femcoders.electronify.product.dto.ProductRequest;
import com.femcoders.electronify.product.dto.ProductResponse;
import com.femcoders.electronify.product.exceptions.NoIdProductFoundException;
import com.femcoders.electronify.product.exceptions.ProductAlreadyExistException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    ProductService productService;

    @Test
    void should_createNewProduct_fromRequest() throws Exception {

        MockMultipartFile mockImg = new MockMultipartFile(
                "image",
                "iphone15.jpg",
                "image/jpeg",
                "fake-image-content".getBytes()
        );

        Map<String, Object> fakeUrl = new HashMap<>();
        fakeUrl.put("secure_url", "https://res.cloudinary.com/demo/image/upload/iphone15.jpg");
        Mockito.when(cloudinaryService.uploadFile(mockImg)).thenReturn(fakeUrl);

        Category category = new Category(1L, "phone", new ArrayList<>());
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Mockito.when(productRepository.findByName("Iphone 15")).thenReturn(Optional.empty());


        ProductRequest productRequest = new ProductRequest("Iphone 15", 850, mockImg, true, 1L);
        Product productToSave = ProductMapper.toEntity(productRequest, "https://res.cloudinary.com/demo/image/upload/iphone15.jpg", category );
        productToSave.setId(1L);
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(productToSave);


        ProductResponse actualResponse = productService.createNewProduct(productRequest);


        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productCaptor.capture());
        Product savedProduct = productCaptor.getValue();

        assertEquals("Iphone 15", savedProduct.getName());
        assertEquals(850, savedProduct.getPrice());
        assertEquals("https://res.cloudinary.com/demo/image/upload/iphone15.jpg", savedProduct.getImageUrl());
        assertEquals(true, savedProduct.isFeatured());
        assertEquals(category, savedProduct.getCategory());
        savedProduct.setId(1L);
        ProductResponse expectedResponse = ProductMapper.fromEntity(savedProduct);


        assertEquals(expectedResponse, actualResponse);
    }


    @Test
    void should_throwException_when_productAlreadyExist() throws Exception {

        MockMultipartFile mockImg = new MockMultipartFile(
                "image",
                "iphone15.jpg",
                "image/jpeg",
                "fake-image-content".getBytes()
        );

        Category category = new Category(1L, "phone", new ArrayList<>());

        ProductRequest productRequest = new ProductRequest("Iphone 15", 850, mockImg, true, 1L);
        Product existingProduct = new Product(1L, "Iphone 15", 850, "https://res.cloudinary.com/demo/image/upload/iphone15.jpg", true, category, 0, 0, new ArrayList<>());

        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        Mockito.when(productRepository.findByName("Iphone 15")).thenReturn(Optional.of(existingProduct));

        ProductAlreadyExistException exception = assertThrows(ProductAlreadyExistException.class, () -> {
            productService.createNewProduct(productRequest);
        });

        assertEquals("This product already exist with id 1. Name: Iphone 15, Price: 850.0.", exception.getMessage());
    }

    @Test
    void should_throwException_when_categoryAlreadyExist() throws Exception{

        MockMultipartFile mockImg = new MockMultipartFile(
                "image",
                "iphone15.jpg",
                "image/jpeg",
                "fake-image-content".getBytes()
        );

        ProductRequest productRequest = new ProductRequest("Iphone 15", 850, mockImg, true, 1L);

        NoIdProductFoundException exception = assertThrows(NoIdProductFoundException.class, () -> {
            productService.createNewProduct(productRequest);
        });

        assertEquals("This product already exist with id 1. Name: Iphone 15, Price: 850.0.", exception.getMessage());

    }

}
