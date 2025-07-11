package com.linktic.prueba.productos.controller;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.linktic.prueba.productos.dto.PaginaApiResponse;
import com.linktic.prueba.productos.dto.ProductoRequest;
import com.linktic.prueba.productos.dto.ProductoResponse;
import com.linktic.prueba.productos.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Operaciones relacionadas con productos")
public class ProductoController {
	
	@Autowired
	private ProductoService service;
	
	@GetMapping
	@Operation(summary = "Listar productos", description = "Retorna una lista paginada de productos con filtros opcionales")
    public PaginaApiResponse<ProductoResponse> listar(
    		@Parameter(description = "Filtro por nombre") @RequestParam(required = false) String nombre, 
    		@Parameter(description = "Filtro por código de barras") @RequestParam(required = false) String codigoBarras, 
    		Pageable pageable, 
    		HttpServletRequest request
    ) {
		Page<ProductoResponse> pagina = service.listar(nombre, codigoBarras, pageable);
		
	    String params = "";
	    if (nombre != null) params += "&nombre=" + nombre;
	    if (codigoBarras != null) params += "&codigoBarras=" + codigoBarras;
		
		String baseUrl = request.getRequestURL().toString();
		String self = baseUrl + "?page=" + pagina.getNumber() + "&size=" + pagina.getSize() + params;
		String next = pagina.hasNext() ? baseUrl + "?page=" + (pagina.getNumber() + 1) + "&size=" + pagina.getSize() + params : null;
		String prev = pagina.hasPrevious() ? baseUrl + "?page=" + (pagina.getNumber() - 1) + "&size=" + pagina.getSize() + params : null;
		
		PaginaApiResponse.Meta meta = new PaginaApiResponse.Meta(pagina.getNumber(), pagina.getSize(), pagina.getTotalElements(), pagina.getTotalPages());
		PaginaApiResponse.Links links = new PaginaApiResponse.Links(self, next, prev);
        return new PaginaApiResponse<ProductoResponse>(pagina.getContent(), meta, links);
    }
	
	@GetMapping("/{id}")
	@Operation(summary = "Obtener producto por ID")
	@ApiResponse(responseCode = "200", description = "Producto encontrado")
	@ApiResponse(responseCode = "404", description = "Producto no encontrado")
	public ResponseEntity<ProductoResponse> obtener(@PathVariable UUID id) {
        Optional<ProductoResponse> producto = service.obtenerPorId(id);
        return producto
        		.map(ResponseEntity::ok)
        		.orElse(ResponseEntity.notFound().build());
    }
	
	@PostMapping
	@Operation(summary = "crear un nuevo producto")
	@ApiResponse(responseCode = "201", description = "Producto creado")
	@ApiResponse(responseCode = "422", description = "falta información del producto")
	@ApiResponse(responseCode = "409", description = "Código de barras duplicado")
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
        ProductoResponse creado = service.crear(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
	
	@PutMapping("/{id}")
	@Operation(summary = "actualiza un producto")
	@ApiResponse(responseCode = "200", description = "Producto modificado")
	@ApiResponse(responseCode = "422", description = "falta información del producto")
	@ApiResponse(responseCode = "409", description = "Código de barras duplicado")
    public ResponseEntity<ProductoResponse> actualizar(@PathVariable UUID id, @Valid @RequestBody ProductoRequest request) {
        ProductoResponse actualizado = service.actualizar(id, request);
        return ResponseEntity.ok(actualizado);
    }
	
	@DeleteMapping("/{id}")
	@Operation(summary = "elimina un producto por su ID")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
