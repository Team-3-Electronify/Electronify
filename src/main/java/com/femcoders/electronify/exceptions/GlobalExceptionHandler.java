package com.femcoders.electronify.exceptions;

import com.femcoders.electronify.cart.exeptions.CartNotFoundException;
import com.femcoders.electronify.cart.exeptions.ItemNotFoundException;
import com.femcoders.electronify.product.exceptions.NoIdProductFoundException;
import com.femcoders.electronify.product.exceptions.ProductAlreadyExistException;
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
}
