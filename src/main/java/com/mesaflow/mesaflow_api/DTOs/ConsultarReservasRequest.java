package com.mesaflow.mesaflow_api.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsultarReservasRequest {

  // swagger
  @Schema(
    description = "ID del usuario que realiza la consulta. Por ahora se envía en el body; en el futuro podría obtenerse desde autenticación/JWT.",
    example = "14",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotNull(message = "Debe indicar el usuario solicitante.")
  private Integer idUsuarioSolicitante;

  // swagger
  @Schema(
    description = "ID del establecimiento a consultar. Se utiliza en la consulta por establecimiento.",
    example = "15"
  )
  private Integer idEstablecimiento;

  // swagger
  @Schema(
    description = "ID del usuario cuyas reservas se desean consultar. Se utiliza en la consulta por usuario.",
    example = "1"
  )
  private Integer idUsuario;

  // swagger
  @Schema(
    description = "Objeto con los filtros aplicables a la consulta de reservas."
  )
  @Valid
  private Filtro filtro;

  @Getter
  @Setter
  public static class Filtro {

    // swagger
    @Schema(
      description = "Estado por el cual se desea filtrar. Valores permitidos: todas, pendiente, confirmada, cancelada, no_show.",
      example = "todas"
    )
    private String estado;
  }
}