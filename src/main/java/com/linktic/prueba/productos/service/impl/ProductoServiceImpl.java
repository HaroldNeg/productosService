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
import com.linktic.prueba.productos.exception.ConflictException;
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

	@Override
	public ProductoResponse consultar(String codigoBarras, String nombre) {
		Producto producto = repository.findByCodigoYNombre(codigoBarras, nombre).orElseThrow(() -> new IllegalArgumentException("El producto no existe"));
		producto.setStock(producto.getStock()-producto.getCompraTemporal());
		return mapper.map(producto, ProductoResponse.class);
	}
	
	public Producto consultarInterno(String codigoBarras, int cantidad) {
		if(cantidad <= 0) throw new IllegalArgumentException("No se puede agregar cantidades iguales o inferiores a 0");
		Producto producto = repository.findByCodigoBarras(codigoBarras).orElseThrow(() -> new IllegalArgumentException("El producto no existe"));
		if ((producto.getStock() - producto.getCompraTemporal()) < cantidad) throw new ConflictException("No hay suficientes unidades del producto");
		return producto;
	}
	
	public void SeparaInventario(UUID id, boolean cancela, int cantidad) {
		Producto producto = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("El producto no existe"));
		if(cancela) producto.setCompraTemporal(producto.getCompraTemporal() - cantidad);
		else producto.setCompraTemporal(producto.getCompraTemporal() + cantidad);
		repository.save(producto);
	}
	
	public void ModificarInventario(UUID id, int cantidad) {
		Producto producto = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("El producto no existe"));
		producto.setStock(producto.getStock() - cantidad);
		producto.setCompraTemporal(producto.getCompraTemporal() - cantidad);
		repository.save(producto);
	}
}
