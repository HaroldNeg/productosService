package com.linktic.prueba.productos.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.linktic.prueba.productos.dto.ProductoRequest;
import com.linktic.prueba.productos.dto.ProductoResponse;

public interface ProductoService {
	
	Page<ProductoResponse> listar(String nombre, String codigoBarras, Pageable pageable);

    Optional<ProductoResponse> obtenerPorId(UUID id);

    ProductoResponse crear(ProductoRequest request);

    ProductoResponse actualizar(UUID id, ProductoRequest request);

    void eliminar(UUID id);
}
