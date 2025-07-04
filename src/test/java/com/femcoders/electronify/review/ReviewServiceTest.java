package com.femcoders.electronify.review;

import com.femcoders.electronify.product.Product;
import com.femcoders.electronify.product.ProductRepository;
import com.femcoders.electronify.product.ProductService;
import com.femcoders.electronify.product.exceptions.NoIdProductFoundException;
import com.femcoders.electronify.review.dto.ReviewRequest;
import com.femcoders.electronify.review.dto.ReviewResponse;
import com.femcoders.electronify.user.UserRepository;
import com.femcoders.electronify.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductService productService;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReviewService reviewService;

    private User testUser;
    private Product testProduct;
    private Review testReview;
    private ReviewRequest reviewRequest;

    @BeforeEach
    public void setUp() {
        reviewService = Mockito.spy(new ReviewService(reviewRepository,
                productRepository,
                userRepository,
                productService));

        testUser = new User();
        testUser.setId(10L);
        testUser.setUsername("testUser");

        testProduct = Product.builder()
                .id(100L)
                .name("Test Product")
                .price(100.0)
                .imageUrl("img.jpg")
                .featured(false)
                .rating(0.0)
                .reviewCount(0)
                .build();

        testReview = Review.builder()
                .id(1L)
                .rating(4.5)
                .body("Great product!")
                .product(testProduct)
                .user(testUser)
                .build();

        reviewRequest = new ReviewRequest(4.5, "Great product!", 100L);
    }

    @Test
    void createReviewTest() {
        Mockito.doReturn(testUser).when(reviewService).getAuthenticatedUser();
        Mockito.when(productRepository.findById(100L)).thenReturn(Optional.of(testProduct));
        Mockito.when(reviewRepository.save(any(Review.class))).thenReturn(testReview);

        ReviewResponse response = reviewService.createReview(reviewRequest);

        assertNotNull(response);
        assertEquals(4.5, response.rating());
        assertEquals(1L, response.id());
        assertEquals("Great product!", response.body());

        Mockito.verify(productService).updateProductStats(100L);
    }

    @Test
    void createReview_WhenProductNotFound() {
        Mockito.when(productRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(NoIdProductFoundException.class,
                () -> reviewService.createReview(reviewRequest));
    }

    @Test
    void getReviewsByProductIdTest_Success() {
        Mockito.when(reviewRepository
                .findByProduct_Id(100L))
                .thenReturn(List.of(testReview));

        List<ReviewResponse> responses = reviewService.getReviewsByProductId(100L);

        assertEquals(1, responses.size());
        assertEquals("Great product!", responses.get(0).body());
    }
}