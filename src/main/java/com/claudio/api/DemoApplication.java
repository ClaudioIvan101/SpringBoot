package com.claudio.api;

import com.claudio.api.model.Product;
import com.claudio.api.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(ProductRepository repository) {
		return (args) -> {
			// Esto se ejecuta solo al arrancar la app
			repository.save(new Product("Coca-Cola", 3.99, 100));
			System.out.println("✅ DATOS DE PRUEBA CARGADOS: Producto ID 1 creado con 100 de stock.");
		};
	}
}
