package com.femcoders.electronify.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.femcoders.electronify.user.dto.UserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        @Transactional
        @WithMockUser(roles = { "ADMIN" })
        void getAllUsers() throws Exception {
                mockMvc.perform(get("/api/users")
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(4)))
                                .andExpect(jsonPath("$[0].username").value("testuser"))
                                .andExpect(jsonPath("$[0].email").value("test@example.com"))
                                .andExpect(jsonPath("$[1].username").value("adminuser"))
                                .andExpect(jsonPath("$[1].email").value("admin@example.com"))
                                .andExpect(jsonPath("$[2].username").value("john"))
                                .andExpect(jsonPath("$[2].email").value("john@example.com"))
                                .andExpect(jsonPath("$[3].username").value("existinguser"))
                                .andExpect(jsonPath("$[3].email").value("existing@example.com"));
        }

        @Test
        @Transactional
        @WithMockUser(roles = { "ADMIN" })
        void getUserById() throws Exception {
                Long userId = 1L;
                mockMvc.perform(get("/api/users/{id}", userId)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(userId))
                                .andExpect(jsonPath("$.username").value("testuser"))
                                .andExpect(jsonPath("$.email").value("test@example.com"))
                                .andExpect(jsonPath("$.reviews", hasSize(2)))
                                .andExpect(jsonPath("$.reviews[0].rating").value(4.5))
                                .andExpect(jsonPath("$.reviews[0].body").value("Great phone, excellent camera!"))
                                .andExpect(jsonPath("$.reviews[0].productid").value(1L))
                                .andExpect(jsonPath("$.reviews[1].rating").value(4.6))
                                .andExpect(jsonPath("$.reviews[1].body").value("Amazing display quality"))
                                .andExpect(jsonPath("$.reviews[1].productid").value(2L));
        }

        @Test
        @Transactional
        @WithMockUser(roles = { "ADMIN" })
        void updateUser() throws Exception {
                Long userId = 1L;
                UserRequest request = new UserRequest("updateduser", "updated@example.com", "newpassword");

                mockMvc.perform(put("/api/users/{id}", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(userId))
                                .andExpect(jsonPath("$.username").value("updateduser"))
                                .andExpect(jsonPath("$.email").value("updated@example.com"));
        }

        @Test
        @Transactional
        @WithMockUser(roles = { "ADMIN" })
        void deleteUser() throws Exception {
                Long userId = 1L;

                mockMvc.perform(delete("/api/users/{id}", userId))
                                .andExpect(status().isNoContent());
        }

        @Test
        @Transactional
        @WithMockUser(roles = { "ADMIN" })
        void should_returnNotFound_whenUserNotExists() throws Exception {
                Long userId = 999L;

                mockMvc.perform(get("/api/users/{id}", userId))
                                .andExpect(status().isNotFound());
        }

        @Test
        @Transactional
        @WithMockUser(roles = { "ADMIN" })
        void should_returnNotFound_whenDeletingNonExistentUser() throws Exception {
                Long userId = 999L;

                mockMvc.perform(delete("/api/users/{id}", userId))
                                .andExpect(status().isNotFound());
        }
}