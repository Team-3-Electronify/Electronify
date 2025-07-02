package com.femcoders.electronify.product;

import com.femcoders.electronify.category.dto.CategoryRequest;
import com.femcoders.electronify.product.dto.ProductRequest;
import com.femcoders.electronify.product.dto.ProductResponse;
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
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProductsList(){
        List<ProductResponse> products = productService.findAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.findProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ProductResponse>> getProductsByFilters(@RequestBody Optional<String> name,
                                                                      @RequestBody Optional<Long> categoryId,
                                                                      @RequestBody Optional<String> priceGroup,
                                                                      @RequestBody Optional<String> sortByPrice,
                                                                      @RequestBody Optional<String> sortByRating){
        List<ProductResponse> products = productService.findProductsByFilters( name, categoryId, priceGroup, sortByPrice,sortByRating);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
    public ResponseEntity<ProductResponse> updateProductById(@PathVariable Long id,@Valid @ModelAttribute ProductRequest productRequest){
        try {
            ProductResponse updatedProduct = productService.updateProduct(id, productRequest);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable Long id){
        productService.deleteProductById(id);
        return new ResponseEntity<>("Product with id " + id + " has been deleted", HttpStatus.NO_CONTENT);
    }

}
