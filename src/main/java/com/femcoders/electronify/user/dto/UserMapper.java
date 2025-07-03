package com.femcoders.electronify.user.dto;

import com.femcoders.electronify.review.Review;
import com.femcoders.electronify.user.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserResponse toDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail());
    }

    public User toEntity(UserRequest request) {
        if (request == null) {
            return null;
        }
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        return user;
    }

    public void updateEntityFromDto(UserRequest request, User user) {
        if (request == null || user == null) {
            return;
        }
        user.setUsername(request.username());
        user.setEmail(request.email());
    }

    public UserWithReviewsResponse toUserWithReviewsResponse(User user, List<Review> reviews) {
        List<UserReviewResponse> reviewResponses = reviews.stream()
                .map(review -> new UserReviewResponse(
                        review.getId(),
                        review.getRating(),
                        review.getBody(),
                        review.getProduct().getId()
                ))
                .collect(Collectors.toList());

        return new UserWithReviewsResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                reviewResponses
        );
    }
}
