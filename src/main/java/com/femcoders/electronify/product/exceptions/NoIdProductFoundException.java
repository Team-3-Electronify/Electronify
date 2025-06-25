package com.femcoders.electronify.product.exceptions;

import com.femcoders.electronify.exceptions.AppException;

public class NoIdProductFoundException extends AppException {
    public NoIdProductFoundException(Long id) {
      super("The product with id: " + id + " does not exist.");
    }
}
