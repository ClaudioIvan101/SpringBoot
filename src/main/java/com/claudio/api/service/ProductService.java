package com.claudio.api.service;

import com.claudio.api.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final List<Product> products = new ArrayList<>();

    public ProductService() {
        products.add(new Product(1, "Coca Cola", 10.0));
        products.add(new Product(2, "Sprite", 20.0));
        products.add(new Product(3, "Fanta", 30.0));
    }

    public List<Product> getAllProducts() {
        return products;
    }

    public List<Product> searchById(Integer id) {
    return products.stream()
            .filter(p -> p.getId() == id)
            .toList();
    }

    public Product createProduct(Product newProduct) {
        int newId = products.size() + 1;
        newProduct.setId(newId);
        products.add(newProduct);
        return newProduct;
    }

    public Optional<Product> updateProduct(Integer id, Product updateData) {
        Optional<Product> found = products.stream()
                .filter(p -> p.getId() == id)
                .findFirst();

        if (found.isPresent()) {
            Product p = found.get();
            p.setName(updateData.getName());
            p.setPrice(updateData.getPrice());
            return Optional.of(p);
        }
        return Optional.empty();
    }

    public boolean deleteProduct(Integer id) {
        return products.removeIf(p -> p.getId() == id);
    }
}
