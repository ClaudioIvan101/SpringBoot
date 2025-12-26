package com.claudio.api.test;
import com.claudio.api.model.Product;
import com.claudio.api.repository.ProductRepository;
import com.claudio.api.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    // Test para comprar un producto exitosamente
    @Test
    void testBuyProduct_Success() throws Exception {
        // Preparación del escenario
        Product mockProduct = new Product("Pepsi", 4.99, 10);
        mockProduct.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);

        // WHEN
        Product result = productService.buyProduct(1L);

        // THEN
        assertEquals(9, result.getStock());
        verify(productRepository, times(1)).save(mockProduct);
        System.out.println("TEST: Compra exitosa (Stock descontado)");
    }

    // test para intentar comprar un producto sin stock
    @Test
    void testBuyProduct_OutOfStock() {
        // GIVEN
        Product mockProduct = new Product("Coca-Cola", 10.0, 0);
        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

        // WHEN & THEN
        Exception exception = assertThrows(RuntimeException.class, () -> {
            productService.buyProduct(1L);
        });

        assertEquals("Out of stock", exception.getMessage());
        verify(productRepository, never()).save(any());
        System.out.println("TEST : Compra bloqueada por falta de stock");
    }

    // Test para listar todos los productos
    @Test
    void getAllProducts_ShouldReturnList() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(new Product(), new Product()));

        List<Product> products = productService.getAllProducts();

        assertEquals(2, products.size());
        System.out.println("TEST: Listar todos los productos");
    }

    // Test para crear un nuevo producto
    @Test
    void createProduct_ShouldSaveAndReturn() {
        Product newProduct = new Product("Galletas", 2.50, 20);
        when(productRepository.save(any(Product.class))).thenReturn(newProduct);

        Product created = productService.createProduct(newProduct);

        assertNotNull(created);
        assertEquals("Galletas", created.getName());
        verify(productRepository).save(any(Product.class));
        System.out.println("TEST: Crear producto");
    }

    // test para buscar un producto por ID
    @Test
    void searchById_ShouldReturnList_WhenFound() {
        Product mockProduct = new Product("Laptop", 1000.0, 5);
        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

        List<Product> result = productService.searchById(1L);

        assertFalse(result.isEmpty());
        assertEquals("Laptop", result.get(0).getName());
        System.out.println("TEST: Buscar por ID");
    }

    // test para actualizar un producto existente
    @Test
    void updateProduct_ShouldUpdate_WhenExists() {
        Product existing = new Product("Viejo", 10.0, 1);
        Product updates = new Product("Nuevo", 20.0, 5);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);

        Optional<Product> result = productService.updateProduct(1L, updates);

        assertTrue(result.isPresent());
        assertEquals("Nuevo", result.get().getName());
        assertEquals(20.0, result.get().getPrice());
        System.out.println("TEST: Actualizar producto");
    }

    // test para borrar un producto por ID
    @Test
    void deleteProduct_ShouldCallRepo() {
        productService.deleteProduct(1L);
        verify(productRepository).deleteById(1L);
        System.out.println("TEST: Borrar producto");
    }
}
