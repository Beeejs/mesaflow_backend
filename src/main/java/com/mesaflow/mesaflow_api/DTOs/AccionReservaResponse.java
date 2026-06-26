package com.mesaflow.mesaflow_api.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AccionReservaResponse {
  // swagger
  @Schema(
    description = "ID de la reserva sobre la cual se ejecutó la acción.",
    example = "31"
  )
  private Integer idReserva;

  // swagger
  @Schema(
    description = "Código numérico del nuevo estado de la reserva. 0=PENDIENTE, 1=CONFIRMADA, 2=CANCELADA, 3=NO_SHOW.",
    example = "1"
  )
  private Integer estado;

  // swagger
  @Schema(
    description = "Descripción textual del nuevo estado de la reserva.",
    example = "CONFIRMADA"
  )
  private String estadoDescripcion;
}