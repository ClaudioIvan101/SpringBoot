package com.claudio.api.test;
import com.claudio.api.model.Product;
import com.claudio.api.repository.ProductRepository;
import com.claudio.api.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    // Con esto estoy creando un repositorio "falso"
    @Mock
    private ProductRepository productRepository;

    // Aqui enyecto el repo falso dentro de mi servicio real
    @InjectMocks
    private ProductService productService;

    @Test
    void testBuyProduct_Success() throws Exception {
        // Preparación del escenario
        Product mockProduct = new Product("Pepsi", 4.99, 10);
        mockProduct.setId(1L);
        // Aqui lo que hago es buscar el id 1, que devuelve una Pesis con 10 de stock
        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));
        // Si se guarda algo, devuelve el mismo objeto
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);

        // WHEN (Ejecución)
        Product result = productService.buyProduct(1L);
        // THEN (Verificación)
        assertEquals(9, result.getStock());
        verify(productRepository, times(1)).save(mockProduct);

        System.out.println("TEST EXITOSO: Stock descontado correctamente");
    }

    @Test
    void testBuyProduct_OutOfStock() {
        // 1. GIVEN: Producto con stock 0
        Product mockProduct = new Product("Coca-Cola", 10.0, 0);
        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

        // 2. WHEN & THEN: Esperamos que explote con RuntimeException
        Exception exception = assertThrows(RuntimeException.class, () -> {
            productService.buyProduct(1L);
        });

        assertEquals("Out of stock", exception.getMessage());

        // Verificamos que NUNCA se intentó guardar en base de datos
        verify(productRepository, never()).save(any());

        System.out.println("Test Exitoso: Compra bloqueada por falta de stock.");
    }
}
