package com.femcoders.electronify.cart.exeptions;

import com.femcoders.electronify.exceptions.AppException;

public class ItemNotFoundException extends AppException {
    public ItemNotFoundException(Long itemId) {
        super("The cart for user: " + itemId + " does not exist.");
    }
}
