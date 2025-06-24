package com.femcoders.electronify.product.dto;

import com.femcoders.electronify.product.Product;
import com.femcoders.electronify.review.dto.ReviewResponse;

import java.util.List;

public class ProductMapper {
    public static Product toEntity(ProductRequest productRequest ){
        return new Product(productRequest.name(), productRequest.price(), productRequest.imageUrl(), productRequest.featured(), productRequest.categoryId());
    }

    public static ProductResponse fromEntity(Product product){

      /*  List<ReviewResponse> reviews = product.getReviews()
                .stream()
                .map(Review::getReview)
                .map(review ->  MapperReviewDto.fromEntity(review))
                .toList(); */
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), product.isFeatured(), product.getCategory(), product.getRating(), product.getReviewCount() //, reviews
        );

    }
}
