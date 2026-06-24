package com.mesaflow.mesaflow_api.Exception;

import com.mesaflow.mesaflow_api.DTOs.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // Manejo de excepciones para RuntimeException
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ApiResponse<Object>> manejarRuntimeException(RuntimeException ex) {
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(ApiResponse.error(ex.getMessage()));
  }

  // Manejo de excepciones para errores de validación
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Object>> manejarErroresDeValidacion(
    MethodArgumentNotValidException ex
  ) {
    String mensaje = ex.getBindingResult()
      .getFieldErrors()
      .stream()
      .findFirst()
      .map(error -> error.getDefaultMessage())
      .orElse("Los datos enviados no son válidos.");

    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(ApiResponse.error(mensaje));
  }

  // Manejo de excepciones para errores de formato de solicitud
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiResponse<Object>> manejarErrorDeFormato(
    HttpMessageNotReadableException ex
  ) {
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(ApiResponse.error("El formato de la solicitud no es válido o contiene campos no permitidos."));
  }
}