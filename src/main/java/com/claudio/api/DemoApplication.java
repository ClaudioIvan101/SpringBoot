package com.claudio.api;

import com.claudio.api.model.Product;
import com.claudio.api.repository.ProductRepository;
import com.claudio.api.repository.UserRepository;
import com.claudio.api.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableRetry
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	@Bean
	public CommandLineRunner loadData(ProductRepository productRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return (args) -> {
			// Volviendo a cargar un producto de ejemplo si la tabla está vacía
			if (productRepository.count() == 0) {
				productRepository.save(new Product("Coca-Cola", 10.0, 100));
				System.out.println("PRODUCTO CREADO");
			}

			// Cargo un Usuario Admin (NUEVO)
			if (userRepository.count() == 0) {
				// Encripto la contraseña antes de guardarla
				String passEncriptada = passwordEncoder.encode("admin123");

				User admin = new User("Claudio", passEncriptada, "ADMIN");
				userRepository.save(admin);

				System.out.println("USUARIO ADMIN CREADO (User: Claudio / Pass: admin123)");
			}
		};
}
}

