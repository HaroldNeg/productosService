package com.linktic.prueba.productos.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "producto")
public class Producto {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	@NotBlank
	private String nombre;
	
	@Column(unique = true, nullable = false)
	private String codigoBarras;
	
	@NotNull
	@Min(0)
	private Double precio;
	
	@NotNull
	private Integer stock;

	@NotNull
	@Min(0)
	private Integer compraTemporal = 0;
}
