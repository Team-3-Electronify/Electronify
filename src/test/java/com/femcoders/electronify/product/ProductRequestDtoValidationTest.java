package com.femcoders.electronify.product;

import com.femcoders.electronify.product.dto.ProductRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductRequestDtoValidationTest {

     private static Validator validator;

     @BeforeAll
    static void init(){
          validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void should_validationNameNotBlankFailed_then_throwError(){
        MockMultipartFile mockImg = new MockMultipartFile(
                "image",
                "iphone15.jpg",
                "image/jpeg",
                "fake-image-content".getBytes()
        );
        ProductRequest productRequest = new ProductRequest("",1.50,mockImg,true,1L);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);

        assertTrue(violations.stream()
                .anyMatch(v ->
                        v.getPropertyPath().toString().equals("name") &&
                        v.getMessage().contains("Name is required")
                ));

    }

    @Test
    void should_validationShortNameSiceFailed_then_throwError(){
        MockMultipartFile mockImg = new MockMultipartFile(
                "image",
                "iphone15.jpg",
                "image/jpeg",
                "fake-image-content".getBytes()
        );
        ProductRequest productRequest = new ProductRequest("H",1.50,mockImg,true,1L);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);

        assertTrue(violations.stream()
                .anyMatch(v ->
                        v.getPropertyPath().toString().equals("name") &&
                                v.getMessage().contains("Name must contain min 2 and max 50 characters")
                ));

    }

    @Test
    void should_validationLongNameSiceFailed_then_throwError(){
        MockMultipartFile mockImg = new MockMultipartFile(
                "image",
                "iphone15.jpg",
                "image/jpeg",
                "fake-image-content".getBytes()
        );
        ProductRequest productRequest = new ProductRequest("123456789012345678901234567890123456789012345678901234567890",1.50,mockImg,true,1L);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);

        assertTrue(violations.stream()
                .anyMatch(v ->
                        v.getPropertyPath().toString().equals("name") &&
                                v.getMessage().contains("Name must contain min 2 and max 50 characters")
                ));

    }

    @Test
    void should_validationPriceFailed_then_throwError(){
        MockMultipartFile mockImg = new MockMultipartFile(
                "image",
                "iphone15.jpg",
                "image/jpeg",
                "fake-image-content".getBytes()
        );
        ProductRequest productRequest = new ProductRequest("product",-1.50,mockImg,true,1L);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);

        assertTrue(violations.stream()
                .anyMatch(v ->
                        v.getPropertyPath().toString().equals("price") &&
                                v.getMessage().contains("The number entered must be positive")
                ));

    }

    @Test
    void should_validationImageFailed_then_throwError(){
        MockMultipartFile mockImg = new MockMultipartFile(
                "image",
                "iphone15.jpg",
                "image/jpeg",
                "fake-image-content".getBytes()
        );
        ProductRequest productRequest = new ProductRequest("product",1.50,null,true,1L);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);

        assertTrue(violations.stream()
                .anyMatch(v ->
                        v.getPropertyPath().toString().equals("image")
                ));

    }

    @Test
    void should_validationCategoryIdLess1Failed_then_throwError(){
        MockMultipartFile mockImg = new MockMultipartFile(
                "image",
                "iphone15.jpg",
                "image/jpeg",
                "fake-image-content".getBytes()
        );
        ProductRequest productRequest = new ProductRequest("product",1.50,mockImg,true,0L);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);

        assertTrue(violations.stream()
                .anyMatch(v ->
                        v.getPropertyPath().toString().equals("categoryId") &&
                                v.getMessage().contains("The category ID cannot be less than 1")
                ));

    }

    @Test
    void should_validationCategoryIdNullFailed_then_throwError(){
        MockMultipartFile mockImg = new MockMultipartFile(
                "image",
                "iphone15.jpg",
                "image/jpeg",
                "fake-image-content".getBytes()
        );
        ProductRequest productRequest = new ProductRequest("product",1.50,mockImg,true,null);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);

        assertTrue(violations.stream()
                .anyMatch(v ->
                        v.getPropertyPath().toString().equals("categoryId") &&
                                v.getMessage().contains("Category ID cannot be null")
                ));

    }

}
