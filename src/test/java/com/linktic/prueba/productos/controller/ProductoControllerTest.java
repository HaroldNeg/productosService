package com.linktic.prueba.productos.controller;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linktic.prueba.productos.dto.ProductoRequest;
import com.linktic.prueba.productos.dto.ProductoResponse;
import com.linktic.prueba.productos.service.ProductoService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SuppressWarnings("removal")
@WebMvcTest(controllers = ProductoController.class)
public class ProductoControllerTest {
	@Autowired
    private MockMvc mockMvc;

	@MockBean
    private ProductoService service;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void obtener_producto_existente_devuelve_200() throws Exception {
        UUID id = UUID.randomUUID();
        ProductoResponse response = new ProductoResponse();
        response.setId(id);
        response.setNombre("Test Producto");
        response.setCodigoBarras("123456");

        Mockito.when(service.obtenerPorId(id)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/productos/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Test Producto"));
    }
    
    @Test
    void obtener_producto_inexistente_devuelve_404() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.when(service.obtenerPorId(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/productos/{id}", id))
            .andExpect(status().isNotFound());
    }
    
    @Test
    void crear_producto_valido_devuelve_201() throws Exception {
        ProductoRequest request = new ProductoRequest("Producto 1", "123", 1000.0, 10);
        ProductoResponse response = new ProductoResponse(UUID.randomUUID(), "Producto 1", "123", 1000.0, 10);

        Mockito.when(service.crear(any())).thenReturn(response);

        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());
    }

    @Test
    void crear_producto_con_datos_invalidos_devuelve_422() throws Exception {
        ProductoRequest request = new ProductoRequest("", "", -10.0, -5);

        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void crear_producto_con_codigo_duplicado_devuelve_409() throws Exception {
        ProductoRequest request = new ProductoRequest("Producto 1", "123", 1000.0, 10);
        Mockito.when(service.crear(any())).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isConflict());
    }
    
    @Test
    void actualizar_producto_valido_devuelve_200() throws Exception {
        UUID id = UUID.randomUUID();
        ProductoRequest request = new ProductoRequest("Producto 1", "123", 1000.0, 10);
        ProductoResponse response = new ProductoResponse(id, "Producto 1", "123", 1000.0, 10);

        Mockito.when(service.actualizar(eq(id), any())).thenReturn(response);

        mockMvc.perform(put("/api/productos/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void actualizar_producto_con_datos_invalidos_devuelve_422() throws Exception {
        UUID id = UUID.randomUUID();
        ProductoRequest request = new ProductoRequest("", "", -1.0, -1);

        mockMvc.perform(put("/api/productos/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        	.andExpect(status().isUnprocessableEntity());
    }

    @Test
    void actualizar_producto_con_codigo_duplicado_devuelve_409() throws Exception {
        UUID id = UUID.randomUUID();
        ProductoRequest request = new ProductoRequest("Producto", "123", 1000.0, 10);
        Mockito.when(service.actualizar(eq(id), any())).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(put("/api/productos/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isConflict());
    }

    @Test
    void eliminar_producto_existente_devuelve_204() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/productos/" + id))
            .andExpect(status().isNoContent());
    }

    @Test
    void listar_productos_devuelve_200() throws Exception {
        ProductoResponse producto = new ProductoResponse(UUID.randomUUID(), "Producto", "123", 1000.0, 10);
        var page = new PageImpl<>(Collections.singletonList(producto), PageRequest.of(0, 5), 1);
        Mockito.when(service.listar(any(), any(), any())).thenReturn(page);

        mockMvc.perform(get("/api/productos?page=0&size=5"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data[0].nombre").value("Producto"));
    }
}
