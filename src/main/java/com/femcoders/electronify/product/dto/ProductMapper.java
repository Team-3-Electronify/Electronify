package com.femcoders.electronify.product.dto;


import com.femcoders.electronify.product.Product;


public class ProductMapper {
    public static Product toEntity(ProductRequest productRequest ){
        Product product =  Product.builder()
                .name(productRequest.name())
                .price(productRequest.price())
                .imageUrl(productRequest.imageUrl())
                .featured(productRequest.featured())
                .id(productRequest.categoryId())
                .build();

        return product;
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
