package com.femcoders.electronify.category;

import com.femcoders.electronify.category.dto.CategoryRequest;
import com.femcoders.electronify.category.dto.CategoryResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategoryList(){
        List<CategoryResponse> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<CategoryResponse> postNewCategory(@Valid @RequestBody CategoryRequest categoryRequest){
        CategoryResponse newCategory = categoryService.createNewCategory(categoryRequest);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategoryById(@PathVariable Long id, @Valid @RequestBody CategoryRequest categoryRequest){
        CategoryResponse updatedCategory = categoryService.updateCategory(id, categoryRequest);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategoryById(@PathVariable Long id){
        categoryService.deleteCategoryById(id);
        return new ResponseEntity<>("Category with id " + id + " has been deleted", HttpStatus.NO_CONTENT);
    }
    
    
}
