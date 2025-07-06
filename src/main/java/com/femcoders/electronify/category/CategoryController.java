package com.femcoders.electronify.category;

import com.femcoders.electronify.category.dto.CategoryRequest;
import com.femcoders.electronify.category.dto.CategoryResponse;
import com.femcoders.electronify.category.dto.CategoryWithProductsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Categories", description = "Operations related to categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "Get all categories",
            responses = {
                    @ApiResponse(responseCode = "200", description = "All categories returned successfully"),
                    @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalServerError")
            })
    public ResponseEntity<List<CategoryResponse>> getAllCategoryList() {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get categories by Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Category found successfully"),
                    @ApiResponse(responseCode = "404", ref = "#/components/responses/CategoryNotFound"),
                    @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalServerError")
            })
    public ResponseEntity<CategoryWithProductsResponse> getCategoryById(@PathVariable Long id) {
        CategoryWithProductsResponse category = categoryService.findCategoryById(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Post new category",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Category created successfully"),
                    @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest"),
                    @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalServerError")
            })
    public ResponseEntity<CategoryResponse> postNewCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        CategoryResponse newCategory = categoryService.createNewCategory(categoryRequest);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Category updated successfully"),
                    @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest"),
                    @ApiResponse(responseCode = "404", ref = "#/components/responses/CategoryNotFound"),
                    @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalServerError")
            })
    public ResponseEntity<CategoryResponse> updateCategoryById(@PathVariable Long id, @Valid @RequestBody CategoryRequest categoryRequest) {
        CategoryResponse updatedCategory = categoryService.updateCategory(id, categoryRequest);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category by ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
                    @ApiResponse(responseCode = "404", ref = "#/components/responses/CategoryNotFound"),
                    @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalServerError")
            })
    public ResponseEntity<String> deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return new ResponseEntity<>("Category with id " + id + " has been deleted", HttpStatus.NO_CONTENT);
    }
}
