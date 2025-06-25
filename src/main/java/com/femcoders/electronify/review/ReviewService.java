package com.femcoders.electronify.review;

import com.femcoders.electronify.product.Product;
import com.femcoders.electronify.review.dto.ReviewRequest;
import com.femcoders.electronify.review.dto.ReviewResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository, ReviewMapper reviewMapper,
                         ProductRepository productRepository, UserRepository userRepository;) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ReviewResponse createReview(ReviewRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.productId()));

        Review review = reviewMapper.toEntity(request);
        review.setProduct(product);

        Review savedReview = reviewRepository.save(review);
        updateProductStats(product);
        return reviewMapper.toResponse(savedReview);
    }

    @Transactional
    public List<ReviewResponse> getReviewsByProductId(Long productId) {
        if (!reviewRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
        return reviewRepository.findByProductId(productId).stream()
                .map(reviewMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ReviewResponse> getReviewsByUserId(Long userId) {
        if (!reviewRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return reviewRepository.findByUserId(userId).stream()
                .map(reviewMapper::toResponse)
                .collect(Collectors.toList());
    }

    private void updateProductStats(Product product) {
        List<Review> reviews = reviewRepository.findByProductId(product.getId());
        double averageRating = reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);
        double roundedRating = Math.round(averageRating * 10.0) / 10.0;
        product.setReviewCount(reviews.size());
        product.setRating(Math.min(roundedRating, 5.0));
        productRepository.save(product);
    }
}
