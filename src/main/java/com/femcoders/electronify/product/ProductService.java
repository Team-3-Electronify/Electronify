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
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final EntityManager entityManager;
    private final CategoryRepository categoryRepository;
    private final CloudinaryService cloudinaryService;

    @Transactional
    public List<ProductResponse> findAllProducts(){
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()){
            throw new EmptyListException();
        }

        return products.stream()
                .map(product -> ProductMapper.fromEntity(product))
                .toList();
    }

    @Transactional
    public ProductResponse findProductById(Long id){
        Product productById = productRepository.findById(id)
                .orElseThrow(() -> new NoIdProductFoundException(id));

        return ProductMapper.fromEntity(productById);
    }

    @Transactional
    public List<ProductResponse> findProductsByFilters(
            Optional<String> productName,
            Optional<Long> categoryId,
            Optional<String> priceGroup,
            Optional<String> sortByPrice,
            Optional<String> sortByRating
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

        List<Order> orderList = new ArrayList<>();

        sortByPrice.ifPresent(order -> {
            if (order.equalsIgnoreCase("asc")) {
                orderList.add(cBuilder.asc(productRoot.get("price")));
            } else if (order.equalsIgnoreCase("desc")) {
                orderList.add(cBuilder.desc(productRoot.get("price")));
            }
        });

        sortByRating.ifPresent(order -> {
            if (order.equalsIgnoreCase("asc")) {
                orderList.add(cBuilder.asc(productRoot.get("rating")));
            } else if (order.equalsIgnoreCase("desc")) {
                orderList.add(cBuilder.desc(productRoot.get("rating")));
            }
        });

        if (!orderList.isEmpty()) {
            cQuery.orderBy(orderList);
        }

        List<Product> products = entityManager.createQuery(cQuery).getResultList();
        if (products.isEmpty()) {
            throw new EmptyListException();
        }
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
        categoryId.ifPresent(id -> predicates.add(cBuilder.equal(productRoot.get("category").get("id"), id)));
    }

    private void filterPriceGroup(Optional<String> priceGroup, CriteriaBuilder cBuilder, Root<Product> productRoot, List<Predicate> predicates) {
        priceGroup.ifPresent(group -> predicates.add(createPricePredicate(cBuilder, productRoot.get("price"), group)));
    }

    @Transactional
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

    @Transactional
    public ProductResponse createNewProduct(ProductRequest productRequest) throws IOException {
        Category isExistingCategory = categoryRepository.findById(productRequest.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(productRequest.categoryId()));
        Optional<Product> isExistingProduct = productRepository.findByName(productRequest.name());
        if (isExistingProduct.isPresent()){
            throw new ProductAlreadyExistException(isExistingProduct.get().getName(),isExistingProduct.get().getPrice(), isExistingProduct.get().getId());
        }

        Map uploadResult = cloudinaryService.uploadFile(productRequest.image());
        String imageUrl = (String) uploadResult.get("secure_url");

        Product newProduct = ProductMapper.toEntity(productRequest, imageUrl, isExistingCategory);
        Product savedProduct = productRepository.save(newProduct);
        return ProductMapper.fromEntity(savedProduct);
    }

    @Transactional
    public ProductResponse updateProduct (Long id, ProductRequest productRequest) throws IOException {
        Category isExistingCategory = categoryRepository.findById(productRequest.categoryId())
                .orElseThrow(() -> new RuntimeException("NO id category found"));
        Optional<Product> isExistingProduct = productRepository.findByName(productRequest.name());
        if (isExistingProduct.isPresent() && !isExistingProduct.get().getId().equals(id)){
            throw new ProductAlreadyExistException(isExistingProduct.get().getName(),isExistingProduct.get().getPrice(), isExistingProduct.get().getId());
        }

        Product productById = productRepository.findById(id)
                .orElseThrow(() -> new NoIdProductFoundException(id));

        productById.setName(productRequest.name().toLowerCase());
        productById.setPrice(productRequest.price());
        productById.setFeatured(productRequest.featured());
        productById.setCategory(isExistingCategory);
        try {
            Map uploadResult = cloudinaryService.uploadFile(productRequest.image());
            String imageUrl = (String) uploadResult.get("secure_url");
            productById.setImageUrl(imageUrl);
        } catch (Exception e) {
            throw new RuntimeException("Error uploading image to Cloudinary", e);
        }

        productRepository.save(productById);
        return ProductMapper.fromEntity(productById);
    }

    public Product updateProductStats(Long idProduct){
        Product isExisting = productRepository.findById(idProduct)
                .orElseThrow(() -> new NoIdProductFoundException(idProduct));

        List<Review> reviews = isExisting.getReviews();

        int updatedReviewCount = reviews.size();
        double averageRating = reviews.stream()
                .mapToDouble(pr -> pr.getRating())
                .average()
                .orElse(0.0);

        averageRating = Math.round(averageRating * 100.0) / 100.0;

        isExisting.setReviewCount(updatedReviewCount);
        isExisting.setRating(averageRating);

        return productRepository.save(isExisting);
    }

    @Transactional
    public void deleteProductById(Long id){
        Product isExisting = productRepository.findById(id)
                .orElseThrow(() -> new NoIdProductFoundException(id));
        String imageUrl = isExisting.getImageUrl();

        String withoutPrefix = imageUrl.substring(imageUrl.indexOf("/upload/") + 8);
        if (withoutPrefix.matches("v\\d+/.+")) {
            withoutPrefix = withoutPrefix.substring(withoutPrefix.indexOf('/') + 1);
        }
        int dotIndex = withoutPrefix.lastIndexOf('.');
        String publicId = (dotIndex != -1) ? withoutPrefix.substring(0, dotIndex) : withoutPrefix;


        try {
            cloudinaryService.deleteFile(publicId);
        } catch (IOException e) {
            throw new RuntimeException("Error deleting image from Cloudinary: " + e.getMessage());
        }

        productRepository.deleteById(id);
    }

}
