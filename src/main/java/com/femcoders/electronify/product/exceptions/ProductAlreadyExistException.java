package com.femcoders.electronify.product.exceptions;

import com.femcoders.electronify.exceptions.AppException;

public class ProductAlreadyExistException extends AppException {
  public ProductAlreadyExistException(String name, double price, Long id) {
    super("This product already exist with id " + id + ". Name: " + name + ", Price: " + price + ".");
  }
}
