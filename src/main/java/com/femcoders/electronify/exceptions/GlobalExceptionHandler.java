package com.femcoders.electronify.exceptions;

import com.femcoders.electronify.cart.exeptions.CartNotFoundException;
import com.femcoders.electronify.cart.exeptions.ItemNotFoundException;
import com.femcoders.electronify.category.exceptions.CategoryAlreadyExistException;
import com.femcoders.electronify.category.exceptions.CategoryNotFoundException;
import com.femcoders.electronify.product.exceptions.NoIdProductFoundException;
import com.femcoders.electronify.product.exceptions.ProductAlreadyExistException;
import com.femcoders.electronify.user.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EmptyListException.class)
    public ResponseEntity<String> handleEmptyList(EmptyListException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(NoIdProductFoundException.class)
    public ResponseEntity<String> handleNoIdProductFound(NoIdProductFoundException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductAlreadyExistException.class)
    public ResponseEntity<String> handleProductAlreadyExist(ProductAlreadyExistException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<String> handleCartNotFoundException(CartNotFoundException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<String> handleItemNotFoundException(ItemNotFoundException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<String> handleCategoryNotFound(CategoryNotFoundException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryAlreadyExistException.class)
    public ResponseEntity<String> handleCategoryAlreadyExist(CategoryAlreadyExistException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String>handleUserNotFoundException(UserNotFoundException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
