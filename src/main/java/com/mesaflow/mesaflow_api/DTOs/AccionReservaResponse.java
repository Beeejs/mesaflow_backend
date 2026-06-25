package com.mesaflow.mesaflow_api.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AccionReservaResponse {

  private Integer idReserva;

  private Integer estado;

  private String estadoDescripcion;
}