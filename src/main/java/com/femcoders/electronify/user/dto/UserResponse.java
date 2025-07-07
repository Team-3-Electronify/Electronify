package com.femcoders.electronify.user.dto;

public record UserResponse(
        Long id,
        String username,
        String email) {
}
