package com.femcoders.electronify.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.femcoders.electronify.user.UserService;
import com.femcoders.electronify.user.dto.UserRequest;
import com.femcoders.electronify.user.dto.UserResponse;
import com.femcoders.electronify.user.dto.UserReviewResponse;
import com.femcoders.electronify.user.dto.UserWithReviewsResponse;
import com.femcoders.electronify.user.exceptions.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    @WithMockUser(roles = { "ADMIN" })
    void getAllUsers() throws Exception {
        List<UserResponse> users = List.of(
                new UserResponse(1L, "user1", "user1@example.com"),
                new UserResponse(2L, "user2", "user2@example.com"));
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[0].email").value("user1@example.com"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].username").value("user2"))
                .andExpect(jsonPath("$[1].email").value("user2@example.com"));

        verify(userService).getAllUsers();
    }

    @Test
    @WithMockUser(roles = { "ADMIN" })
    void getUserById() throws Exception {
        Long userId = 1L;
        List<UserReviewResponse> reviews = List.of(
                new UserReviewResponse(1L, 4.5, "Great product!", 1L),
                new UserReviewResponse(2L, 5.0, "Excellent!", 2L));
        UserWithReviewsResponse userWithReviews = new UserWithReviewsResponse(
                userId, "testuser", "test@example.com", reviews);
        when(userService.getUserById(userId)).thenReturn(userWithReviews);

        mockMvc.perform(get("/api/users/{id}", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.reviews", hasSize(2)))
                .andExpect(jsonPath("$.reviews[0].id").value(1L))
                .andExpect(jsonPath("$.reviews[0].rating").value(4.5))
                .andExpect(jsonPath("$.reviews[0].body").value("Great product!"))
                .andExpect(jsonPath("$.reviews[0].productid").value(1L))
                .andExpect(jsonPath("$.reviews[1].id").value(2L))
                .andExpect(jsonPath("$.reviews[1].rating").value(5.0))
                .andExpect(jsonPath("$.reviews[1].body").value("Excellent!"))
                .andExpect(jsonPath("$.reviews[1].productid").value(2L));

        verify(userService).getUserById(userId);
    }

    @Test
    @WithMockUser(roles = { "ADMIN" })
    void updateUser() throws Exception {
        Long userId = 1L;
        UserRequest request = new UserRequest("updateduser", "updated@example.com", "newpassword");
        UserResponse updatedUser = new UserResponse(userId, "updateduser", "updated@example.com");

        when(userService.updateUser(eq(userId), any(UserRequest.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value("updateduser"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));

        verify(userService).updateUser(eq(userId), any(UserRequest.class));
    }

    @Test
    @WithMockUser(roles = { "ADMIN" })
    void deleteUser() throws Exception {
        Long userId = 999L;
        doThrow(new UserNotFoundException(userId)).when(userService).deleteUser(userId);

        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNotFound());

        verify(userService).deleteUser(userId);
    }
}