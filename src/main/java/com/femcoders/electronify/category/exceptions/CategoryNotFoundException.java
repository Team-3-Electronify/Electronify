package com.femcoders.electronify.category.exceptions;

import com.femcoders.electronify.exceptions.AppException;

public class CategoryNotFoundException extends AppException {
    public CategoryNotFoundException(Long id) {
        super("The category with id: " + id + " does not exist.");
    }
}
