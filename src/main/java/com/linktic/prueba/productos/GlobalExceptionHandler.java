package com.linktic.prueba.productos;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.linktic.prueba.productos.dto.ErrorResponse;
import com.linktic.prueba.productos.exception.ConflictException;
import com.linktic.prueba.productos.exception.ResourceNotFoundException;

import feign.FeignException;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	//400 - Mala petición
	@ExceptionHandler({MissingServletRequestParameterException.class, IllegalArgumentException.class, ConstraintViolationException.class})
	public ResponseEntity<Map<String, List<ErrorResponse>>> handleBadRequest(Exception ex) {
		return buildError("400", "Bad Request", ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

    // 404 - No encontrado
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, List<ErrorResponse>>> handleNotFound(ResourceNotFoundException ex) {
    	return buildError("404", "Not Found", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // 405 - Método no permitido
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, List<ErrorResponse>>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
    	return buildError("405", "Method Not Allowed", ex.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    // 409 - Conflicto (por ejemplo, registro duplicado)
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, List<ErrorResponse>>> handleConflict(ConflictException ex) {
    	return buildError("409", "Conflict", ex.getMessage(), HttpStatus.CONFLICT);
    }

    // 409 - Conflicto (por ejemplo, registro duplicado)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, List<ErrorResponse>>> handleConflict(DataIntegrityViolationException ex) {
    	String error = Optional.ofNullable(ex.getRootCause())
                .map(Throwable::getMessage)
                .map(String::toLowerCase)
                .orElse("");
    	String msg = "";
        if (error.contains("ya existe la llave (codigo_barras)")) msg = "Ya existe un producto con ese código de barras.";
        return buildError("409", "Conflict", msg, HttpStatus.CONFLICT);
    }

	// 422
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<ErrorResponse>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<ErrorResponse> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> new ErrorResponse("422", "Validation Error",  err.getField() + ": " + err.getDefaultMessage()))
                .collect(Collectors.toList());

        return ResponseEntity.unprocessableEntity().body(Map.of("errors", errors));
    }

	// 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, List<ErrorResponse>>> handleGeneralErrors(Exception ex) {
        return buildError("500", "Internal Server Error", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Void> handleFaviconRequest(NoResourceFoundException ex) throws NoResourceFoundException {
        if (ex.getResourcePath().equals("favicon.ico")) {
            return ResponseEntity.notFound().build();
        }
        throw ex;
    }
    
    private ResponseEntity<Map<String, List<ErrorResponse>>> buildError(String status, String title, String detail, HttpStatus httpStatus) {
        ErrorResponse error = new ErrorResponse(status, title, detail);
        return ResponseEntity.status(httpStatus).body(Map.of("errors", List.of(error)));
    }

}
