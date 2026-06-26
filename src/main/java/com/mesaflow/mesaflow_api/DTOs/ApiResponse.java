package com.mesaflow.mesaflow_api.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

  // swagger
  @Schema(
    description = "Indica si la operación fue exitosa.",
    example = "true"
  )
  private Boolean success;

  // swagger
  @Schema(
    description = "Contenido de la respuesta cuando la operación fue exitosa."
  )
  private T response;

  // swagger
  @Schema(
    description = "Mensaje de error cuando la operación falla.",
    example = "El filtro de estado indicado no es válido."
  )
  private String message;

  // Constructor cuando es OK
  public static <T> ApiResponse<T> ok(T response) {
    return new ApiResponse<>(true, response, null);
  }

  // Constructor cuando es ERROR
  public static <T> ApiResponse<T> error(String message) {
    return new ApiResponse<>(false, null, message);
  }
}