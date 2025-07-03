package com.femcoders.electronify.user.dto;

import java.util.List;

public record UserWithReviewsResponse(
        Long id,
        String username,
        String email,
        List<UserReviewResponse>reviews) {
}
