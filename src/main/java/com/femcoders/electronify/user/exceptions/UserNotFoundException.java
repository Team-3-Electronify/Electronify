package com.femcoders.electronify.user.exceptions;

import com.femcoders.electronify.exceptions.AppException;

public class UserNotFoundException extends AppException {
    public UserNotFoundException (Long id) {
        super("User not found with id: " + id);
    }
}
