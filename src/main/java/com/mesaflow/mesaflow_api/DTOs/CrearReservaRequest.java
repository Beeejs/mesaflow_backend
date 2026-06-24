package com.mesaflow.mesaflow_api.DTOs;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CrearReservaRequest {

  @NotNull(message = "Debe indicar un usuario.")
  private Integer idUsuario;

  @NotNull(message = "Debe indicar un establecimiento.")
  private Integer idEstablecimiento;

  @NotNull(message = "Debe indicar fecha y hora de reserva.")
  @Future(message = "La fecha de reserva debe ser futura.")
  private LocalDateTime fechaHoraInicio;

  @NotNull(message = "Debe indicar la cantidad de comensales.")
  @Positive(message = "La cantidad de comensales debe ser mayor a cero.")
  private Integer comensales;
}