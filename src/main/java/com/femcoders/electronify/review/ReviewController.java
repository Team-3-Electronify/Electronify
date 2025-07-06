package com.femcoders.electronify.review;

import com.femcoders.electronify.review.dto.ReviewRequest;
import com.femcoders.electronify.review.dto.ReviewResponse;
import com.femcoders.electronify.swagger.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "Get all reviews by user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Reviews returned successfully"),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalServerError")
            })
    public ResponseEntity<List<ReviewResponse>> getReviewsByUserIdList(@RequestParam Long userId) {
        List<ReviewResponse> reviews = reviewService.getReviewsByUserId(userId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/byProduct")
    @Operation(summary = "Get all reviews by product",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Reviews returned successfully"),
                    @ApiResponse(responseCode = "404", description = "Product not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalServerError")
            })
    public ResponseEntity<List<ReviewResponse>> getReviewsByProductIdList(@RequestParam Long productId) {
        List<ReviewResponse> reviews = reviewService.getReviewsByProductId(productId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Post new review by product",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Review successfully was created."),
                    @ApiResponse(responseCode = "404", ref = "#/components/responses/NotFound"),
                    @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest"),
                    @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalServerError")
            })
    public ResponseEntity<ReviewResponse> postNewReview(@Valid @RequestBody ReviewRequest reviewRequest) {
        ReviewResponse newReview = reviewService.createReview(reviewRequest);
        return new ResponseEntity<>(newReview, HttpStatus.CREATED);
    }
}

