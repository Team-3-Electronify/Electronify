package com.femcoders.electronify.category.exceptions;

import com.femcoders.electronify.exceptions.AppException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryNotFoundExceptionTest {
    @Test
    void should_createException() {
        Long id = 1L;

        CategoryNotFoundException exception = new CategoryNotFoundException(id);

        assertEquals("The category with id: 1 does not exist.", exception.getMessage());
        assertInstanceOf(AppException.class, exception);
        assertInstanceOf(RuntimeException.class, exception);
    }
}