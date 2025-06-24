package com.femcoders.electronify.product;

import com.femcoders.electronify.category.Category;
import com.femcoders.electronify.product.dto.ProductMapper;
import com.femcoders.electronify.product.dto.ProductRequest;
import com.femcoders.electronify.product.dto.ProductResponse;
import com.femcoders.electronify.review.Review;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> getAllProducts(){
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()){
            throw new RuntimeException("Empty list");
        }

        return products.stream()
                .map(product -> ProductMapper.fromEntity(product))
                .toList();
    }

    public ProductResponse getProductById(Long id){
        Product productById = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No id product found"));

        return ProductMapper.fromEntity(productById);
    }

    public ProductResponse createNewProduct(ProductRequest productRequest){
       /* Category isExistingCategory = categoryRepository.findById(productRequest.categoryId())
                .orElseThrow(() -> new RuntimeException("NO id category found")); */
        Optional<Product> isExistingProduct = productRepository.findByName(productRequest.name());
        if (isExistingProduct.isPresent()){
            throw new RuntimeException("This product already exist");
        }
        Product newProduct = ProductMapper.toEntity(productRequest);
        Product savedProduct = productRepository.save(newProduct);
        return ProductMapper.fromEntity(savedProduct);
    }

    public ProductResponse updateProduct (Long id, ProductRequest productRequest){
        /* Category isExistingCategory = categoryRepository.findById(productRequest.categoryId())
                .orElseThrow(() -> new RuntimeException("NO id category found")); */
        Optional<Product> isExistingProduct = productRepository.findByName(productRequest.name());
        if (isExistingProduct.isPresent() && !isExistingProduct.get().getId().equals(id)){
            throw new RuntimeException("This product already exist");
        }

        Product productById = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No id product found"));

        productById.setName(productRequest.name());
        productById.setPrice(productRequest.price());
        productById.setImageUrl(productRequest.imageUrl());
        productById.setFeatured(productRequest.featured());
        //productById.setCategory(isExistingCategory);

        productRepository.save(productById);
        return ProductMapper.fromEntity(productById);
    }

   /* public Product updateProductStats(Long idProduct){
        Product isExisting = productRepository.findById(idProduct)
                .orElseThrow(() -> new RuntimeException("No id product found"));

        List<Review> reviews = isExisting.getReviews();

        int updatedReviewCount = reviews.size();
        double averageRating = reviews.stream()
                .mapToDouble(pr -> pr.getReview().getRating())
                .average()
                .orElse(0.0);

        averageRating = Math.round(averageRating * 100.0) / 100.0;

        isExisting.setReviewCount(updatedReviewCount);
        isExisting.setRating(averageRating);

        return productRepository.save(isExisting);
    }   */

    public void deletePRoductById(Long id){
        Product isExisting = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No id product found"));
        productRepository.deleteById(id);
    }

}
