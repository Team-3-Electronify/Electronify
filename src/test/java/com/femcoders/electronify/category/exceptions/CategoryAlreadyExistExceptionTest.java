package com.femcoders.electronify.category.exceptions;

import com.femcoders.electronify.exceptions.AppException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryAlreadyExistExceptionTest {
    @Test
    void should_createException() {
        String name = "Mobiles";
        Long id = 1L;

        CategoryAlreadyExistException exception = new CategoryAlreadyExistException(name, id);

        assertEquals("This category already exist with id 1. Name: Mobiles", exception.getMessage());
        assertInstanceOf(AppException.class, exception);
        assertInstanceOf(RuntimeException.class, exception);
    }
}