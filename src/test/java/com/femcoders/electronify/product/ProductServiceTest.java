package com.femcoders.electronify.product;

import com.femcoders.electronify.category.Category;
import com.femcoders.electronify.category.CategoryRepository;
import com.femcoders.electronify.category.exceptions.CategoryNotFoundException;
import com.femcoders.electronify.cloudinary.CloudinaryService;
import com.femcoders.electronify.exceptions.EmptyListException;
import com.femcoders.electronify.product.dto.ProductMapper;
import com.femcoders.electronify.product.dto.ProductRequest;
import com.femcoders.electronify.product.dto.ProductResponse;
import com.femcoders.electronify.product.exceptions.NoIdProductFoundException;
import com.femcoders.electronify.product.exceptions.ProductAlreadyExistException;
import com.femcoders.electronify.review.Review;
import com.femcoders.electronify.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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
    void should_throwException_when_categoryNotFound() throws Exception{

        MockMultipartFile mockImg = new MockMultipartFile(
                "image",
                "iphone15.jpg",
                "image/jpeg",
                "fake-image-content".getBytes()
        );

        ProductRequest productRequest = new ProductRequest("Iphone 15", 850, mockImg, true, 1L);

        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> {
            productService.createNewProduct(productRequest);
        });

        assertEquals("The category with id: 1 does not exist.", exception.getMessage());

    }

    @Test
    void should_findAllProducts() throws Exception{
        Category category = new Category(1L, "phone", new ArrayList<>());

        Product product1 = new Product(1L,"Iphone 15", 850, "https://res.cloudinary.com/demo/image/upload/iphone15.jpg", true, category,0,0,new ArrayList<>());
        Product product2 = new Product(2L,"Iphone 10", 1050, "https://res.cloudinary.com/demo/image/upload/iphone15.jpg", true, category,0,0,new ArrayList<>());

        List<Product> products = List.of(product1, product2);
        Mockito.when(productRepository.findAll()).thenReturn(products);

        List<ProductResponse> actualResponse = productService.findAllProducts();

        List<ProductResponse> expectedresponses = products.stream()
                .map(ProductMapper::fromEntity)
                .toList();

        assertEquals(expectedresponses, actualResponse);

    }

    @Test
    void should_findAllProducts_when_emptyList() throws Exception{
        Mockito.when(productRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(EmptyListException.class, () -> {
            productService.findAllProducts();
        });

    }

    @Test
    void should_findProductById() throws Exception{
        Category category = new Category(1L, "phone", new ArrayList<>());

        Product product1 = new Product(1L,"Iphone 15", 850, "https://res.cloudinary.com/demo/image/upload/iphone15.jpg", true, category,0,0,new ArrayList<>());

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        ProductResponse expectedresponse = ProductMapper.fromEntity(product1);
        ProductResponse actualResponse = productService.findProductById(1L);



        assertEquals(expectedresponse, actualResponse);

    }

    @Test
    void should_findProductById_NoIdProductFoundException() throws Exception{

        assertThrows(NoIdProductFoundException.class, () -> {
            productService.findProductById(1L);
        });

    }

    @Test
    void should_updateProduct_fromRequest() throws Exception {

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


        ProductRequest existingProduct = new ProductRequest("Iphone 15", 850, mockImg, true, 1L);
        Product productToSave = ProductMapper.toEntity(existingProduct, "https://res.cloudinary.com/demo/image/upload/iphone15.jpg", category );
        productToSave.setId(1L);
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(productToSave);


        productService.createNewProduct(existingProduct);

        ProductRequest updatedRequest = new ProductRequest("Iphone 15 Pro", 950, mockImg, true, 1L);

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(productToSave));

        productService.updateProduct(1L, updatedRequest);


        Product actualResponse =new Product(1L,"Iphone 15 Pro", 950, "https://res.cloudinary.com/demo/image/upload/iphone15.jpg", true, category,0,0,new ArrayList<>());

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository, times(2)).save(productCaptor.capture());

        List<Product> savedProducts = productCaptor.getAllValues();
        Product savedProduct = savedProducts.get(1);

        assertEquals("iphone 15 pro", savedProduct.getName());
        assertEquals(950, savedProduct.getPrice());
    }

    @Test
    void should_deleteProduct_fromRequest() throws Exception {

        Category category = new Category(1L, "phone", new ArrayList<>());

        Product existingProduct = new Product(1L,"Iphone 15", 850, "https://res.cloudinary.com/demo/image/upload/iphone15.jpg", true, category,0,0,new ArrayList<>());

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));

        productService.deleteProductById(1L);

        verify(cloudinaryService).deleteFile("iphone15");

        verify(productRepository).deleteById(1L);


    }

    @Test
    void should_updateProductStats_when_userPostNewReview(){
        Category category = new Category(1L, "phone", new ArrayList<>());

        Product existingProduct = new Product(1L,"Iphone 15", 850, "https://res.cloudinary.com/demo/image/upload/iphone15.jpg", true, category,0,0,new ArrayList<>());

        User user = new User(); user.setId(1L); user.setUsername("username");

        Review review = new Review(1L,5,"Good!!", existingProduct, user);

        existingProduct.getReviews().add(review);

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenAnswer(i -> i.getArgument(0));

        Product productUpdate = productService.updateProductStats(1L);

        assertEquals(5.0,productUpdate.getRating());
        assertEquals(1,productUpdate.getReviewCount());
    }

}
