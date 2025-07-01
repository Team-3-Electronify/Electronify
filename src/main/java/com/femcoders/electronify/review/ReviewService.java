package com.femcoders.electronify.review;

import com.femcoders.electronify.product.Product;
import com.femcoders.electronify.product.ProductRepository;
import com.femcoders.electronify.product.ProductService;
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
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductService productService;

    @Transactional
    public ReviewResponse createReview(ReviewRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + request.productId()));
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + request.userId()));
        Review review = ReviewMapper.toEntity(request);
        review.setProduct(product);
        review.setUser(user);
        Review savedReview = reviewRepository.save(review);
        productService.updateProductStats(request.productId());
        return ReviewMapper.toResponse(savedReview);
    }

    @Transactional
    public List<ReviewResponse> getReviewsByProductId(Long productId) {
        if (reviewRepository.findByProduct_Id(productId).isEmpty()) {
            throw new RuntimeException("Product not found with id: " + productId);
        }
        return reviewRepository.findByProduct_Id(productId).stream()
                .map(ReviewMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ReviewResponse> getReviewsByUserId(Long userId) {
        if (reviewRepository.findByUser_Id(userId).isEmpty()) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        return reviewRepository.findByUser_Id(userId).stream()
                .map(ReviewMapper::toResponse)
                .collect(Collectors.toList());
    }

}
