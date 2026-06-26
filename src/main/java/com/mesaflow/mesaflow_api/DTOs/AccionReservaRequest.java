package com.mesaflow.mesaflow_api.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccionReservaRequest {
  // swagger
  @Schema(
    description = "ID del usuario que solicita ejecutar la acción sobre la reserva.",
    example = "14",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotNull(message = "Debe indicar el usuario que realiza la acción.")
  private Integer idUsuario;

  // swagger
  @Schema(
    description = "Acción a ejecutar sobre la reserva. Valores permitidos: CONFIRMAR, CANCELAR.",
    example = "CONFIRMAR",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotBlank(message = "Debe indicar la acción a realizar.")
  private String scope;
}