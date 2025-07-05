package com.femcoders.electronify.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.femcoders.electronify.user.UserService;
import com.femcoders.electronify.user.dto.UserRequest;
import com.femcoders.electronify.user.dto.UserResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {

        @Autowired
        @SuppressWarnings("unused")
        private MockMvc mockMvc;

        @Autowired
        @SuppressWarnings("unused")
        private ObjectMapper objectMapper;

        @MockitoBean
        @SuppressWarnings("unused")
        private UserService userService;

        @MockitoBean
        @SuppressWarnings("unused")
        private AuthenticationManager authenticationManager;

        @Test
        void should_registerUser_successfully() throws Exception {

                UserRequest request = new UserRequest("testuser", "test@example.com", "password123");
                UserResponse userResponse = new UserResponse(1L, "testuser", "test@example.com");

                when(userService.registerUser(any(UserRequest.class))).thenReturn(userResponse);

                mockMvc.perform(post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(1L))
                                .andExpect(jsonPath("$.username").value("testuser"))
                                .andExpect(jsonPath("$.email").value("test@example.com"));

                verify(userService).registerUser(any(UserRequest.class));
        }

        @Test
        void should_return400_when_registerUser_invalidRequest() throws Exception {
                UserRequest invalidRequest = new UserRequest("", "invalid-email", "123");

                mockMvc.perform(post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
                                .andExpect(status().isBadRequest());

                verify(userService, never()).registerUser(any());
        }

        @Test
        void should_return400_when_registerUser_nullRequest() throws Exception {
                mockMvc.perform(post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                                .andExpect(status().isBadRequest());

                verify(userService, never()).registerUser(any());
        }

        @Test
        void should_login_successfully() throws Exception {
                AuthController.LoginRequest loginRequest = new AuthController.LoginRequest("testuser", "password123");
                UserResponse userResponse = new UserResponse(1L, "testuser", "test@example.com");
                Authentication authentication = mock(Authentication.class);

                when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                                .thenReturn(authentication);
                when(userService.getUserByUsername("testuser")).thenReturn(userResponse);

                mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.message").value("Login successful"))
                                .andExpect(jsonPath("$.authenticated").value(true))
                                .andExpect(jsonPath("$.user.id").value(1L))
                                .andExpect(jsonPath("$.user.username").value("testuser"))
                                .andExpect(jsonPath("$.user.email").value("test@example.com"));

                verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
                verify(userService).getUserByUsername("testuser");
        }

        @Test
        void should_return401_when_login_invalidCredentials() throws Exception {

                AuthController.LoginRequest loginRequest = new AuthController.LoginRequest("testuser", "wrongpassword");

                when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                                .thenThrow(new BadCredentialsException("Invalid credentials"));

                mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest)))
                                .andExpect(status().isUnauthorized())
                                .andExpect(jsonPath("$.message").value("Invalid username or password"))
                                .andExpect(jsonPath("$.authenticated").value(false));

                verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
                verify(userService, never()).getUserByUsername(any());
        }

        @Test
        void should_return401_when_login_userNotFound() throws Exception {

                AuthController.LoginRequest loginRequest = new AuthController.LoginRequest("nonexistent",
                                "password123");
                Authentication authentication = mock(Authentication.class);

                when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                                .thenReturn(authentication);
                when(userService.getUserByUsername("nonexistent"))
                                .thenThrow(new UsernameNotFoundException("User not found"));

                mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest)))
                                .andExpect(status().isUnauthorized())
                                .andExpect(jsonPath("$.message").value("Invalid username or password"))
                                .andExpect(jsonPath("$.authenticated").value(false));

                verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
                verify(userService).getUserByUsername("nonexistent");
        }

}