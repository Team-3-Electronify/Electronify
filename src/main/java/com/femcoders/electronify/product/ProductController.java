package com.femcoders.electronify.product;

import com.femcoders.electronify.product.dto.ProductRequest;
import com.femcoders.electronify.product.dto.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProductsList(){
        List<ProductResponse> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> postNewProduct(@Valid @RequestBody ProductRequest productRequest){
        ProductResponse newProduct = productService.createNewProduct(productRequest);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProductById(@PathVariable Long id,@Valid @RequestBody ProductRequest productRequest){
        ProductResponse updatedProduct = productService.updateProduct(id,productRequest);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable Long id){
        productService.deletePRoductById(id);
        return new ResponseEntity<>("Product with id " + id + " has been deleted", HttpStatus.NO_CONTENT);
    }

}
