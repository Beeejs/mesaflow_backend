package com.mesaflow.mesaflow_api.DTOs;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Getter
@Setter
public class CrearReservaRequest {

  // swagger
  @Schema(
    description = "ID del usuario que realiza la reserva.",
    example = "1",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotNull(message = "Debe indicar un usuario.")
  private Integer idUsuario;

  // swagger
  @Schema(
    description = "ID del establecimiento donde se desea realizar la reserva.",
    example = "15",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotNull(message = "Debe indicar un establecimiento.")
  private Integer idEstablecimiento;
  
  // swagger
  @Schema(
    description = "Fecha y hora de inicio de la reserva. Debe ser una fecha futura.",
    example = "2026-07-21T21:00:00",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotNull(message = "Debe indicar fecha y hora de reserva.")
  @Future(message = "La fecha de reserva debe ser futura.")
  private LocalDateTime fechaHoraInicio;

  // swagger
  @Schema(
    description = "Cantidad de comensales de la reserva. Debe ser mayor a cero.",
    example = "4",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotNull(message = "Debe indicar la cantidad de comensales.")
  @Positive(message = "La cantidad de comensales debe ser mayor a cero.")
  private Integer comensales;
}