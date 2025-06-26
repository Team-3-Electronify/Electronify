package com.femcoders.electronify.user.controller;

import com.femcoders.electronify.user.UserService;
import com.femcoders.electronify.user.dto.UserRequest;
import com.femcoders.electronify.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRequest request) {
        UserResponse registeredUser = userService.registerUser(request);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }
}
