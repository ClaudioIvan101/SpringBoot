package com.claudio.api.controller;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {
    // SIMULACIÓN (BORRAR LUEGO)
    private int stock = 100;

    @PostMapping("/compra/{id}")
    public synchronized String comprarProducto(@PathVariable String id) {
     // simulando un retraso de red
     try {
         Thread.sleep(100);
     } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
     }
     if(stock > 0) {
         stock--;
         System.out.println("Venta realizada! Stock restando: " + stock);
         return "Compra exitosa del producto " + id + ". Stock: " + stock;
     } else {
         System.out.println("FALLO: Sin Stock!");
         return "Producto " + id + " agotado."; // Deberia devolver un HTTP
     }
    }

    // ENDPOINT para resetear el stock
    @PostMapping("/reset-stock")
    public String resetStock() {
        this.stock = 100;
        return "Stock reseteado a 100.";
    }
}
