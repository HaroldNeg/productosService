package com.linktic.prueba.productos.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.linktic.prueba.productos.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, UUID>{
	
	@Query("""
	        SELECT p FROM Producto p
	        WHERE (:nombre IS NULL OR UPPER(p.nombre) LIKE CONCAT('%', UPPER(CAST(:nombre AS string)), '%'))
	        AND (:codigoBarras IS NULL OR p.codigoBarras = :codigoBarras)
			""")
	Page<Producto> findAllFilter(@Param("nombre") String nombre, @Param("codigoBarras") String codigoBarras, Pageable pageable);
	
	Optional<Producto> findByCodigoBarras(String codigoBarras);
}
