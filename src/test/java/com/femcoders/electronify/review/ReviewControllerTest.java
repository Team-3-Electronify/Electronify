package com.femcoders.electronify.review;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.femcoders.electronify.review.dto.ReviewRequest;
import com.femcoders.electronify.review.dto.ReviewResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;


@WebMvcTest(ReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReviewService reviewService;

    private ReviewResponse response;

    @BeforeEach
    public void setUp() {
        response = new ReviewResponse(1L,
                4.5,
                "Great Product!",
                100L,
                "testUser");
    }

    @Test
    void getReviewByUserIdTest() throws Exception {
        Mockito.when(reviewService.getReviewsByUserId(10L))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/reviews/byUser")
                        .param("userId", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].rating").value(4.5))
                .andExpect(jsonPath("$[0].body").value("Great Product!"));
    }

    @Test
    void getReviewByProductIdTest() throws Exception {
        Mockito.when(reviewService.getReviewsByProductId(100L))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/reviews/byProduct")
                        .param("productId", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].rating").value(4.5))
                .andExpect(jsonPath("$[0].body").value("Great Product!"));
    }

    @Test
    void postNewReviewTest_Success() throws Exception {
        ReviewRequest request = new ReviewRequest(4.5,
                "Great Product!",
                100L);

        Mockito.when(reviewService.createReview(any(ReviewRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.rating").value(4.5))
                .andExpect(jsonPath("$.body").value("Great Product!"));
    }
}