package com.mesaflow.mesaflow_api.DTOs;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsultarReservasRequest {

  @NotNull(message = "Debe indicar el usuario solicitante.")
  private Integer idUsuarioSolicitante;

  private Integer idEstablecimiento;

  private Integer idUsuario;

  @Valid
  private Filtro filtro;

  @Getter
  @Setter
  public static class Filtro {

    private String estado;
  }
}