package com.femcoders.electronify.product;

import com.femcoders.electronify.product.dto.ProductRequest;
import com.femcoders.electronify.product.dto.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Operations related to products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "Get all products")
    public ResponseEntity<List<ProductResponse>> getAllProductsList(){
        List<ProductResponse> products = productService.findAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.findProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/filter")
    @Operation(summary = "Filter products by category, name and price range and sort from highest to lowest or lowest to highest by price and rating.")
    public ResponseEntity<List<ProductResponse>> getProductsByFilters(@RequestParam Optional<String> name,
                                                                      @RequestParam Optional<Long> categoryId,
                                                                      @RequestParam Optional<String> priceGroup,
                                                                      @RequestParam Optional<String> sortByPrice,
                                                                      @RequestParam Optional<String> sortByRating){
        List<ProductResponse> products = productService.findProductsByFilters( name, categoryId, priceGroup, sortByPrice,sortByRating);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Post a new product")
    public ResponseEntity<ProductResponse> postNewProduct(@Valid @ModelAttribute ProductRequest productRequest){
        try {
            ProductResponse newProduct = productService.createNewProduct(productRequest);
            return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update product by ID")
    public ResponseEntity<ProductResponse> updateProductById(@PathVariable Long id,@Valid @ModelAttribute ProductRequest productRequest){
        try {
            ProductResponse updatedProduct = productService.updateProduct(id, productRequest);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product by ID")
    public ResponseEntity<String> deleteProductById(@PathVariable Long id){
        productService.deleteProductById(id);
        return new ResponseEntity<>("Product with id " + id + " has been deleted", HttpStatus.NO_CONTENT);
    }

}
