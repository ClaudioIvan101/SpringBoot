package com.claudio.api.controller;

import com.claudio.api.model.Product;
import com.claudio.api.utils.AnswerApi;
import com.claudio.api.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<AnswerApi> index() {
        List<Product> products = productService.getAllProducts();
        AnswerApi answerApi = new AnswerApi("List of products", products);
        return ResponseEntity.ok(answerApi);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> showProduct(@PathVariable Long id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().body("Parameter 'id' is required");
        }
        List<Product> filteredProducts = productService.searchById(id);

        AnswerApi answerApi = new AnswerApi("Search results for: " + id, filteredProducts);
        return ResponseEntity.ok(answerApi);
    }

    @PostMapping("/products")
    public ResponseEntity<?> postProduct(@RequestBody Product newProduct) {
        Product createdProduct = productService.createProduct(newProduct);
        return ResponseEntity.status(201).body(createdProduct);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<?> putProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        Optional<Product> result = productService.updateProduct(id, updatedProduct);

        if (result.isPresent()) {
            return ResponseEntity.ok("Product updated: " + result.get().getName());
        } else {
            return ResponseEntity.status(404).body("Product not found");
        }
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
         productService.deleteProduct(id);
            return ResponseEntity.ok("Product deleted with id: " + id);
    }

    @PostMapping("/products/{id}/purchase")
    public ResponseEntity<?> purchaseProduct(@PathVariable Long id) throws Exception {
            Product updatedProduct = productService.buyProduct(id);
            return ResponseEntity.ok("Purchase successful. Remaining stock: " + updatedProduct.getStock());
    }
    // Endpoint auxiliar para resetear y probar muchas veces
    @PostMapping("/init-data")
    public ResponseEntity<?> initData() {
        productService.initdb();
        return ResponseEntity.ok("Database initialized with products");
    }
}