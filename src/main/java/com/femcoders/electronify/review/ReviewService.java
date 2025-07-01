package com.femcoders.electronify.review;

import com.femcoders.electronify.product.Product;
import com.femcoders.electronify.product.ProductRepository;
import com.femcoders.electronify.review.dto.ReviewMapper;
import com.femcoders.electronify.review.dto.ReviewRequest;
import com.femcoders.electronify.review.dto.ReviewResponse;
import com.femcoders.electronify.user.UserRepository;
import com.femcoders.electronify.user.model.User;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReviewResponse createReview(ReviewRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + request.productId()));
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + request.userId()));
        Review review = reviewMapper.toEntity(request);
        review.setProduct(product);
        review.setUser(user);
        Review savedReview = reviewRepository.save(review);
//        updateProductStats(product);
        return reviewMapper.toResponse(savedReview);
    }

    @Transactional
    public List<ReviewResponse> getReviewsByProductId(Long productId) {
        if (reviewRepository.findByProduct_Id(productId).isEmpty()) {
            throw new RuntimeException("Product not found with id: " + productId);
        }
        return reviewRepository.findByProduct_Id(productId).stream()
                .map(reviewMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ReviewResponse> getReviewsByUserId(Long userId) {
        if (reviewRepository.findByUser_Id(userId).isEmpty()) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        return reviewRepository.findByUser_Id(userId).stream()
                .map(reviewMapper::toResponse)
                .collect(Collectors.toList());
    }

//    private void updateProductStats(Product product) {
//        List<Review> reviews = reviewRepository.findByProduct_Id(product.getId());
//        double averageRating = reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);
//        double roundedRating = Math.round(averageRating * 10.0) / 10.0;
//        product.setReviewCount(reviews.size());
//        product.setRating(Math.min(roundedRating, 5.0));
//        productRepository.save(product);
//    }
}
