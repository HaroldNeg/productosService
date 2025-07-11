package com.linktic.prueba.productos.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponse {

	private UUID id;
	private String nombre;
	private String codigoBarras;
	private Double precio;
	private Integer stock;
}
