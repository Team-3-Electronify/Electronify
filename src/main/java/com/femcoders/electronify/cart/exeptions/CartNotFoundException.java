package com.femcoders.electronify.cart.exeptions;

import com.femcoders.electronify.exceptions.AppException;

public class CartNotFoundException extends AppException {
    public CartNotFoundException(String username) {
        super("The cart for user: " + username + " does not exist.");
    }
}
