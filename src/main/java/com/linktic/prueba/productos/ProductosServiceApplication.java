package com.linktic.prueba.productos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class ProductosServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductosServiceApplication.class, args);
	}
	
	@PostConstruct
	public void init() {
		System.out.println("✅ Aplicación iniciada correctamente");
	}

}
