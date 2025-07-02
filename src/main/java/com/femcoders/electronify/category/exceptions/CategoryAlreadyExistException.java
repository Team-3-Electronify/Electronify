package com.femcoders.electronify.category.exceptions;

import com.femcoders.electronify.exceptions.AppException;

public class CategoryAlreadyExistException extends AppException {
    public CategoryAlreadyExistException(String name, Long id) {
        super("This category already exist with id " + id + ". Name: " + name);
    }
}
