package com.femcoders.electronify.category;

import com.femcoders.electronify.category.dto.CategoryRequest;
import com.femcoders.electronify.category.dto.CategoryResponse;
import com.femcoders.electronify.category.dto.CategoryWithProductsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

class CategoryServiceTest {

    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    CategoryService categoryService;

    private Category mobilesCategory;
    private Category computersCategory;
    private Category updatedMobilesCategory;
    private CategoryRequest mobilesRequest;
    private CategoryRequest computersRequest;
    private CategoryRequest updatedMobilesRequest;

    @BeforeEach
    void setUp() {
        mobilesCategory = createNewCategory(1L, "Mobiles");
        computersCategory = createNewCategory(2L, "Computers");
        updatedMobilesCategory = createNewCategory(1L, "Updated Mobiles");
        mobilesRequest = new CategoryRequest("Mobiles");
        computersRequest = new CategoryRequest("Computers");
        updatedMobilesRequest = new CategoryRequest("Updated Mobiles");
    }

    @Test
    void getAllCategories() {
        List<Category> categories = List.of(mobilesCategory, computersCategory);
        when(categoryRepository.findAll()).thenReturn(categories);

        List<CategoryResponse> result = categoryService.getAllCategories();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Mobiles", result.get(0).name());
        assertEquals("Computers", result.get(1).name());
        verify(categoryRepository).findAll();
    }

    @Test
    void findCategoryById() {
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(mobilesCategory));

        CategoryWithProductsResponse result = categoryService.findCategoryById(categoryId);

        assertNotNull(result);
        assertEquals(categoryId, result.id());
        assertEquals("Mobiles", result.name());
        verify(categoryRepository).findById(categoryId);
    }

    @Test
    void createNewCategory() {
        when(categoryRepository.findByName("Mobiles")).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(mobilesCategory);

        CategoryResponse result = categoryService.createNewCategory(mobilesRequest);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Mobiles", result.name());
        verify(categoryRepository).findByName("Mobiles");
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void updateCategory() {
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(mobilesCategory));
        when(categoryRepository.findByName("Updated Mobiles")).thenReturn(Optional.empty());
        when(categoryRepository.save(mobilesCategory)).thenReturn(updatedMobilesCategory);

        CategoryResponse result = categoryService.updateCategory(categoryId, updatedMobilesRequest);

        assertNotNull(result);
        assertEquals(categoryId, result.id());
        assertEquals("Updated Mobiles", result.name());
        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository).findByName("Updated Mobiles");
        verify(categoryRepository).save(mobilesCategory);
    }

    @Test
    void deleteCategoryById() {
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(mobilesCategory));

        assertDoesNotThrow(()-> categoryService.deleteCategoryById(categoryId));

        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository).deleteById(categoryId);
    }

    private Category createNewCategory(Long id, String name) {
        return Category.builder()
                .id(id)
                .name(name)
                .products(new ArrayList<>())
                .build();
    }

}