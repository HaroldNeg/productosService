package com.linktic.prueba.productos.service.impl;

import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.linktic.prueba.productos.dto.ProductoRequest;
import com.linktic.prueba.productos.dto.ProductoResponse;
import com.linktic.prueba.productos.model.Producto;
import com.linktic.prueba.productos.repository.ProductoRepository;
import com.linktic.prueba.productos.service.ProductoService;

@Service
public class ProductoServiceImpl implements ProductoService{
	
	@Autowired
	private ProductoRepository repository;
	
	@Autowired
	private ModelMapper mapper;

	@Override
	public Page<ProductoResponse> listar(String nombre, String codigoBarras, Pageable pageable) {
		if(nombre.isEmpty()) nombre = null;
		if(codigoBarras.isEmpty()) codigoBarras = null;
		return repository
				.findAllFilter(nombre, codigoBarras, pageable)
				.map(producto -> mapper.map(producto, ProductoResponse.class));
	}

	@Override
	public Optional<ProductoResponse> obtenerPorId(UUID id) {
		return repository
				.findById(id)
				.map(producto -> mapper.map(producto, ProductoResponse.class));
	}

	@Override
	public ProductoResponse crear(ProductoRequest request) {
		Producto producto = mapper.map(request, Producto.class);
		producto = repository.save(producto);
		return mapper.map(producto, ProductoResponse.class);
	}

	@Override
	public ProductoResponse actualizar(UUID id, ProductoRequest request) {
		Producto producto = repository
				.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        mapper.map(request, producto);
        return mapper.map(repository.save(producto), ProductoResponse.class);
	}

	@Override
	public void eliminar(UUID id) {
		repository.deleteById(id);
	}

}
