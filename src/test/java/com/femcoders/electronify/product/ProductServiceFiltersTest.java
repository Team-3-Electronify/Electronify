package com.femcoders.electronify.product;

import com.femcoders.electronify.exceptions.EmptyListException;
import com.femcoders.electronify.product.dto.ProductResponse;
import com.femcoders.electronify.category.dto.CategoryResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ProductServiceFiltersTest {

    @Mock
    private ProductService productService;

    @Test
    void should_ReturnFilteredProducts_When_FilterByNameAndSortPriceAsc() {
        CategoryResponse smartphonesCategory = new CategoryResponse(1L, "Smartphones & Accessories");
        ProductResponse iphone = new ProductResponse(1L, "iPhone 15 Pro", 999.99,
                "https://example.com/iphone.jpg", true, smartphonesCategory, 4.5, 128, null);

        Mockito.when(productService.findProductsByFilters(
                        Optional.of("iPhone"), Optional.empty(), Optional.empty(),
                        Optional.of("asc"), Optional.empty()))
                .thenReturn(List.of(iphone));

        List<ProductResponse> result = productService.findProductsByFilters(
                Optional.of("iPhone"),
                Optional.empty(),
                Optional.empty(),
                Optional.of("asc"),
                Optional.empty());

        assertEquals(1, result.size());
        assertEquals("iPhone 15 Pro", result.getFirst().name());

    }

    @Test
    void should_returnFilteredProducts_when_FilterByCategoryAndPriceGroupAndShortPriceDescShortRatingAsc() {
        CategoryResponse watchesCategory = new CategoryResponse(5L, "Watches & Fitness");
        ProductResponse garmin = new ProductResponse(15L, "Garmin Forerunner 265", 449.99,
                "https://example.com/garmin.jpg", false, watchesCategory, 4.3, 25, null);
        ProductResponse applWatch = new ProductResponse(16L, "Apple Watch Series 9", 399.99,
                "https://example.com/applewatch.jpg", false, watchesCategory, 4.4, 30, null);

        Mockito.when(productService.findProductsByFilters(
                        Optional.empty(), Optional.of(5L), Optional.of("300€ - 600€"),
                        Optional.of("desc"), Optional.of("asc")))
                .thenReturn(List.of(garmin, applWatch));

        List<ProductResponse> result = productService.findProductsByFilters(
                Optional.empty(),
                Optional.of(5L),
                Optional.of("300€ - 600€"),
                Optional.of("desc"),
                Optional.of("asc"));

        assertEquals(2, result.size());
        assertEquals("Garmin Forerunner 265", result.get(0).name());
        assertEquals("Apple Watch Series 9", result.get(1).name());
    }

    @Test
    void should_returnFilteredProducts_when_FilterByPriceGroupAndShortPriceDesc() {
        CategoryResponse smartphonesCategory = new CategoryResponse(1L, "Smartphones & Accessories");
        CategoryResponse cameraCategory = new CategoryResponse(6L, "Camera & Photo");

        ProductResponse camera = new ProductResponse(20L, "Sony Alpha a7 IV Camera", 2499.99,
                "https://example.com/sony.jpg", false, cameraCategory, 4.7, 15, null);
        ProductResponse macbook = new ProductResponse(3L, "MacBook Pro 14", 1999.99,
                "https://example.com/macbook.jpg", false, smartphonesCategory, 4.7, 76, null);
        ProductResponse samsung = new ProductResponse(2L, "Samsung Galaxy S24 Ultra", 1199.99,
                "https://example.com/samsung.jpg", true, smartphonesCategory, 4.6, 95, null);
        ProductResponse dell = new ProductResponse(4L, "Dell XPS 13", 1299.99,
                "https://example.com/dell.jpg", false, smartphonesCategory, 4.4, 52, null);
        ProductResponse ipad = new ProductResponse(7L, "iPad Pro 12.9", 1099.99,
                "https://example.com/ipad.jpg", false, smartphonesCategory, 4.6, 63, null);
        ProductResponse surface = new ProductResponse(8L, "Surface Pro 9", 999.99,
                "https://example.com/surface.jpg", false, smartphonesCategory, 4.3, 29, null);

        Mockito.when(productService.findProductsByFilters(
                        Optional.empty(), Optional.empty(), Optional.of("More than 900€"),
                        Optional.of("desc"), Optional.empty()))
                .thenReturn(List.of(camera, macbook, dell, samsung, ipad, surface));

        List<ProductResponse> result = productService.findProductsByFilters(
                Optional.empty(),
                Optional.empty(),
                Optional.of("More than 900€"),
                Optional.of("desc"),
                Optional.empty());

        assertEquals(6, result.size());
        assertEquals("Sony Alpha a7 IV Camera", result.getFirst().name());
    }

    @Test
    void should_throwException_when_FilteredProductsListEmptyList() {
        Mockito.when(productService.findProductsByFilters(
                        Optional.of("Apple"), Optional.empty(), Optional.of("Less than 50€"),
                        Optional.of("asc"), Optional.empty()))
                .thenThrow(new EmptyListException());

        assertThrows(EmptyListException.class, () -> productService.findProductsByFilters(
                Optional.of("Apple"),
                Optional.empty(),
                Optional.of("Less than 50€"),
                Optional.of("asc"),
                Optional.empty()));

    }
}