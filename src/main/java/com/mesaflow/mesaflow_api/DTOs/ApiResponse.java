package com.mesaflow.mesaflow_api.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonInclude;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

  private Boolean success;
  private T response;
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