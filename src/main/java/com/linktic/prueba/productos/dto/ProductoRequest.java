package com.linktic.prueba.productos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoRequest {
	
	@NotBlank
	private String nombre;
	
	@NotBlank
	private String codigoBarras;
	
	@NotNull
	@Min(0)
	private Double precio;
	
	@NotNull
	private Integer stock;
}
