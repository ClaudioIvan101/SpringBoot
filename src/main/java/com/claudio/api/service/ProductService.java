package com.claudio.api.service;

import com.claudio.api.model.Product;
import com.claudio.api.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    // Método para inicializar datos de prueba
    public void initdb() {
        productRepository.save(new Product("Coca-Cola", 2.99, 100));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> searchById(Long id) {
        return productRepository.findById(id)
                .map(producto -> List.of(producto))
                .orElse(Collections.emptyList());
    }

    public Product createProduct(Product newProduct) {
        productRepository.save(newProduct);
        return newProduct;
    }

    public Optional<Product> updateProduct(Long id, Product updateData) {
        Optional<Product> found = productRepository.findById(id);

        if (found.isPresent()) {
            Product p = found.get();
            p.setName(updateData.getName());
            p.setPrice(updateData.getPrice());
            productRepository.save(p);
            return Optional.of(p);
        }
        return Optional.empty();
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    public Product buyProduct(Long id) throws Exception {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new Exception("Product not found"));
        // Simulamos latencia (para que k6 pueda causar colisiones)
        try { Thread.sleep(10); } catch (InterruptedException e) {}
        // Verificamos stock
        if (product.getStock() > 0) {
            product.setStock(product.getStock() - 1);
            // Guardamos. Si la version cambió, JPA lanza ObjectOptimisticLockingFailureException
            return productRepository.save(product);
        } else {
            throw new RuntimeException("Out of stock");
        }
    }
}
