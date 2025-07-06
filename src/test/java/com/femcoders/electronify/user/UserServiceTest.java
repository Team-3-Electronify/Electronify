package com.femcoders.electronify.user;

import com.femcoders.electronify.user.dto.UserRequest;
import com.femcoders.electronify.user.dto.UserResponse;
import com.femcoders.electronify.user.dto.UserWithReviewsResponse;
import com.femcoders.electronify.user.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @Transactional
    void registerUser() {
        UserRequest request = new UserRequest("newuser", "newuser@example.com", "password123");
        UserResponse actualResponse = userService.registerUser(request);

        assertNotNull(actualResponse);
        assertEquals("newuser", actualResponse.username());
        assertEquals("newuser@example.com", actualResponse.email());
        assertNotNull(actualResponse.id());
    }

    @Test
    @Transactional
    void getAllUsers() {
        List<UserResponse> actualResponses = userService.getAllUsers();

        assertNotNull(actualResponses);
        assertEquals(4, actualResponses.size());
        assertEquals("testuser", actualResponses.get(0).username());
        assertEquals("test@example.com", actualResponses.get(0).email());
        assertEquals("adminuser", actualResponses.get(1).username());
        assertEquals("admin@example.com", actualResponses.get(1).email());
        assertEquals("john", actualResponses.get(2).username());
        assertEquals("john@example.com", actualResponses.get(2).email());
        assertEquals("existinguser", actualResponses.get(3).username());
        assertEquals("existing@example.com", actualResponses.get(3).email());
    }

    @Test
    @Transactional
    void getUserById() {
        Long userId = 1L;
        UserWithReviewsResponse actualResponse = userService.getUserById(userId);

        assertNotNull(actualResponse);
        assertEquals(userId, actualResponse.id());
        assertEquals("testuser", actualResponse.username());
        assertEquals("test@example.com", actualResponse.email());
        assertEquals(2, actualResponse.reviews().size());
        assertEquals(4.5, actualResponse.reviews().getFirst().rating());
        assertEquals("Great phone, excellent camera!", actualResponse.reviews().getFirst().body());
        assertEquals(1L, actualResponse.reviews().getFirst().productid());
    }

    @Test
    @Transactional
    void should_throwException_whenUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(999L));
    }

    @Test
    @Transactional
    void loadUserByUsername() {
        String username = "testuser";
        UserDetails userDetails = userService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    @Transactional
    void should_throwException_whenUsernameNotFound() {
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("nonexistent"));
    }

    @Test
    @Transactional
    void updateUser() {
        Long userId = 1L;
        UserRequest request = new UserRequest("updateduser", "updated@example.com", "newpassword");
        UserResponse actualResponse = userService.updateUser(userId, request);

        assertNotNull(actualResponse);
        assertEquals(userId, actualResponse.id());
        assertEquals("updateduser", actualResponse.username());
        assertEquals("updated@example.com", actualResponse.email());
    }

    @Test
    @Transactional
    void deleteUser() {
        Long userId = 1L;
        userService.deleteUser(userId);

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    @Transactional
    void should_throwException_whenDeletingNonExistentUser() {
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(999L));
    }

    @Test
    @Transactional
    void getUserByUsername() {
        String username = "testuser";
        UserResponse actualResponse = userService.getUserByUsername(username);

        assertNotNull(actualResponse);
        assertEquals(username, actualResponse.username());
        assertEquals("test@example.com", actualResponse.email());
    }

    @Test
    @Transactional
    void should_throwException_whenGettingUserByNonExistentUsername() {
        assertThrows(UsernameNotFoundException.class, () -> userService.getUserByUsername("nonexistent"));
    }
}