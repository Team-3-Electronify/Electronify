package com.femcoders.electronify.product;

import com.femcoders.electronify.category.CategoryRepository;
import com.femcoders.electronify.exceptions.EmptyListException;
import com.femcoders.electronify.product.dto.ProductResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ProductServiceTestFilters {
    @Autowired
    private ProductService productService;


    @Test
    void should_ReturnFilteredProducts_When_FilterByNameAndSortPriceAsc() {

        List<ProductResponse> result = productService.findProductsByFilters(
                Optional.of("iPhone"),
                Optional.empty(),
                Optional.empty(),
                Optional.of("asc"),
                Optional.empty()
        );


        assertEquals(1, result.size());
        assertEquals("iPhone 15 Pro", result.get(0).name());

    }

    @Test
    void should_returnFilteredProducts_when_FilterByCategoryAndPriceGroupAndShortPriceDescShortRatingAsc(){

        List<ProductResponse> result = productService.findProductsByFilters(
                Optional.empty(),
                Optional.of(5L),
                Optional.of("300€ - 600€"),
                Optional.of("desc"),
                Optional.of("asc")
        );

        result.forEach(r -> System.out.println(r.name()));

        assertEquals(2, result.size());
        assertEquals("Garmin Forerunner 265", result.get(0).name());
    }

    @Test
    void should_returnFilteredProducts_when_FilterByPriceGroupAndShortPriceDesc(){
        List<ProductResponse> result = productService.findProductsByFilters(
                Optional.empty(),
                Optional.empty(),
                Optional.of("More than 900€"),
                Optional.of("desc"),
                Optional.empty()
        );

        assertEquals(6, result.size());
        assertEquals("Sony Alpha a7 IV Camera", result.get(0).name());
    }

    @Test
    void should_throwException_when_FilteredProductsListEmtyList(){

        assertThrows(EmptyListException.class, () -> {

            productService.findProductsByFilters(
                    Optional.of("Apple"),
                    Optional.empty(),
                    Optional.of("Less than 50€"),
                    Optional.of("asc"),
                    Optional.empty()
            );
        });

    }
}
