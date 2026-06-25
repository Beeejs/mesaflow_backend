package com.mesaflow.mesaflow_api.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccionReservaRequest {

  @NotNull(message = "Debe indicar el usuario que realiza la acción.")
  private Integer idUsuario;

  @NotBlank(message = "Debe indicar la acción a realizar.")
  private String scope;
}