package com.femcoders.electronify.product;

import com.femcoders.electronify.category.dto.CategoryRequest;
import com.femcoders.electronify.exceptions.EmptyListException;
import com.femcoders.electronify.product.dto.ProductMapper;
import com.femcoders.electronify.product.dto.ProductRequest;
import com.femcoders.electronify.product.dto.ProductResponse;
import com.femcoders.electronify.product.exceptions.NoIdProductFoundException;
import com.femcoders.electronify.product.exceptions.ProductAlreadyExistException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final EntityManager entityManager;

    public ProductService(ProductRepository productRepository, EntityManager entityManager) {
        this.productRepository = productRepository;
        this.entityManager = entityManager;
    }

    public List<ProductResponse> findAllProducts(){
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()){
            throw new EmptyListException();
        }

        return products.stream()
                .map(product -> ProductMapper.fromEntity(product))
                .toList();
    }

    public ProductResponse findProductById(Long id){
        Product productById = productRepository.findById(id)
                .orElseThrow(() -> new NoIdProductFoundException(id));

        return ProductMapper.fromEntity(productById);
    }

    public List<ProductResponse> findProductsByFilters(
            Optional<String> productName,
            Optional<Long> categoryId,
            Optional<String> priceGroup
    ){
        CriteriaBuilder cBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> cQuery = cBuilder.createQuery(Product.class);
        Root<Product> productRoot = cQuery.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();
        filterByName(productName, cBuilder, productRoot, predicates);
        filterCategory(categoryId, cBuilder, productRoot, predicates);
        filterPriceGroup(priceGroup, cBuilder, productRoot, predicates);

        if (!predicates.isEmpty()){
            cQuery.where(cBuilder.and(predicates.toArray(predicates.toArray(new Predicate[0]))));
        }

        List<Product> products = entityManager.createQuery(cQuery).getResultList();
       /* if (products.isEmpty()) {
            throw new EmptyListException();
        }*/
        return products.stream()
                .map(product -> ProductMapper.fromEntity(product))
                .toList();

    }

    private void filterByName(Optional<String> productName, CriteriaBuilder cBuilder, Root<Product> productRoot, List<Predicate> predicates) {
        productName.filter(n -> !n.trim().isEmpty())
                .ifPresent(n -> {
                    String searchPattern = "%" + n.toLowerCase() + "%";
                    predicates.add(cBuilder.like(cBuilder.lower(productRoot.get("name")), searchPattern));
                });
    }

    private void filterCategory(Optional<Long> categoryId, CriteriaBuilder cBuilder, Root<Product> productRoot, List<Predicate> predicates) {
       /* if (categoryId.isPresent() && !categoryRepository.existsById(categoryId.get())) {
            throw new EntityNotFoundException("The category whith id " + categoryId.get() + " does not exist.");
        }*/

        categoryId.ifPresent(id -> predicates.add(cBuilder.equal(productRoot.get("category").get("id"), id)));

       /* if (products.isEmpty() && categoryId.isPresent()) {
            throw new EmptyListException();
        }*/
    }

    private void filterPriceGroup(Optional<String> priceGroup, CriteriaBuilder cBuilder, Root<Product> productRoot, List<Predicate> predicates) {
        priceGroup.ifPresent(group -> predicates.add(createPricePredicate(cBuilder, productRoot.get("price"), group)));
    }

    private Predicate createPricePredicate(CriteriaBuilder cBuilder, Path<Double> pricePath, String priceGroup) {
        switch (priceGroup) {
            case "Less than 50€":
                return cBuilder.lessThan(pricePath, 50.0);
            case "50€ - 150€":
                return cBuilder.between(pricePath, 50.0, 150.0);
            case "150€ - 300€":
                return cBuilder.between(pricePath, 150.0, 300.0);
            case "300€ - 600€":
                return cBuilder.between(pricePath, 300.0, 600.0);
            case "600€ - 900€":
                return cBuilder.between(pricePath, 600.0, 900.0);
            case "More than 900€":
                return cBuilder.greaterThan(pricePath, 900.0);
            default:
                return cBuilder.conjunction();
        }
    }

    public List<ProductResponse> getProductsByCategory(CategoryRequest categoryRequest){
        /* Category isExistingCategory = categoryRepository.findByName(categoryRequest.name())
                .orElseThrow(() -> new RuntimeException("Category dont exist"));*/
        List<Product> productsListByCategory = productRepository.getProductsByCategory_Name(categoryRequest.name());

        if (productsListByCategory.isEmpty()){
            throw new EmptyListException();
        }

        return productsListByCategory.stream()
                .map(product -> ProductMapper.fromEntity(product))
                .toList();
    }

    public ProductResponse createNewProduct(ProductRequest productRequest){
       /* Category isExistingCategory = categoryRepository.findById(productRequest.categoryId())
                .orElseThrow(() -> new RuntimeException("NO id category found")); */
        Optional<Product> isExistingProduct = productRepository.findByName(productRequest.name());
        if (isExistingProduct.isPresent()){
            throw new ProductAlreadyExistException(isExistingProduct.get().getName(),isExistingProduct.get().getPrice(), isExistingProduct.get().getId());
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
            throw new ProductAlreadyExistException(isExistingProduct.get().getName(),isExistingProduct.get().getPrice(), isExistingProduct.get().getId());
        }

        Product productById = productRepository.findById(id)
                .orElseThrow(() -> new NoIdProductFoundException(id));

        productById.setName(productRequest.name().toLowerCase());
        productById.setPrice(productRequest.price());
        productById.setImageUrl(productRequest.imageUrl());
        productById.setFeatured(productRequest.featured());
        //productById.setCategory(isExistingCategory);

        productRepository.save(productById);
        return ProductMapper.fromEntity(productById);
    }

   /* public Product updateProductStats(Long idProduct){
        Product isExisting = productRepository.findById(idProduct)
                .orElseThrow(() -> new NoIdProductFoundException(id)););

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
                .orElseThrow(() -> new NoIdProductFoundException(id));
        productRepository.deleteById(id);
    }

}
