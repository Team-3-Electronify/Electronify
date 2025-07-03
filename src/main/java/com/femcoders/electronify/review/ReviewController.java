package com.femcoders.electronify.review;

import com.femcoders.electronify.review.dto.ReviewRequest;
import com.femcoders.electronify.review.dto.ReviewResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Reviews", description = "Operations related to reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/byUser")
    @Operation(summary = "Get all reviews by user")
    public ResponseEntity<List<ReviewResponse>> getReviewsByUserIdList(@RequestParam Long userId) {
        List<ReviewResponse> reviews = reviewService.getReviewsByUserId(userId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/byProduct")
    @Operation(summary = "Get all reviews by product")
    public ResponseEntity<List<ReviewResponse>> getReviewsByProductIdList(@RequestParam Long productId) {
        List<ReviewResponse> reviews = reviewService.getReviewsByProductId(productId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Post new review by product")
    public ResponseEntity<ReviewResponse> postNewReview(@Valid @RequestBody ReviewRequest reviewRequest) {
        ReviewResponse newReview = reviewService.createReview(reviewRequest);
        return new ResponseEntity<>(newReview, HttpStatus.CREATED);
    }
}

