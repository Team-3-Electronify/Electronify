package com.femcoders.electronify.category;

import com.femcoders.electronify.category.dto.CategoryMapper;
import com.femcoders.electronify.category.dto.CategoryRequest;
import com.femcoders.electronify.category.dto.CategoryResponse;
import com.femcoders.electronify.exceptions.EmptyListException;
import com.femcoders.electronify.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryResponse> getAllCategories(){
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()){
            throw new EmptyListException();
        }
        return categories.stream()
                .map(category -> CategoryMapper.fromEntity(category))
                .toList();
    }

    public CategoryResponse createNewCategory(CategoryRequest categoryRequest){
        Optional<Category> isExistingCategory = categoryRepository.findByName(categoryRequest.name());
        if (isExistingCategory.isPresent()){
            throw new RuntimeException("Category exist");
        }
        Category newCategory = CategoryMapper.toEntity(categoryRequest);
        Category savedCategory = categoryRepository.save(newCategory);
        return CategoryMapper.fromEntity(savedCategory);
    }

    public CategoryResponse updateCategory(Long idCategory, CategoryRequest categoryRequest){
        Category isExisting = categoryRepository.findById(idCategory)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + idCategory));
        Optional<Category> isExistingCategory = categoryRepository.findByName(categoryRequest.name());
        if (isExistingCategory.isPresent()){
            throw new RuntimeException("Category exist");
        }
        isExisting.setName(categoryRequest.name());
        Category savedCategory = categoryRepository.save(isExisting);
        return CategoryMapper.fromEntity(savedCategory);
    }

    public void deleteCategoryById(Long id){
        Category isExisting = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        categoryRepository.deleteById(id);
    }
}
