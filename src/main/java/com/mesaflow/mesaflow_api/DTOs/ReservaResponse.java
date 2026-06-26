package com.mesaflow.mesaflow_api.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@AllArgsConstructor
public class ReservaResponse {
  // swagger
  @Schema(description = "ID de la reserva creada o consultada.", example = "31")
  private Integer idReserva;

  // swagger
  @Schema(description = "ID del usuario dueño de la reserva.", example = "1")
  private Integer idUsuario;

  // swagger
  @Schema(description = "Nombre completo del usuario dueño de la reserva.", example = "Juan Perez")
  private String nombreUsuario;

  // swagger
  @Schema(description = "ID del establecimiento de la reserva.", example = "15")
  private Integer idEstablecimiento;

  // swagger
  @Schema(description = "Nombre del establecimiento.", example = "Burger Station")
  private String nombreEstablecimiento;

  // swagger
  @Schema(description = "Fecha y hora de inicio de la reserva.", example = "2026-07-21T21:00:00")
  private LocalDateTime fechaHoraInicio;

  // swagger
  @Schema(description = "Fecha y hora de finalización calculada según la configuración del establecimiento.", example = "2026-07-21T23:00:00")
  private LocalDateTime fechaHoraFinCalculada;

  // swagger
  @Schema(description = "Cantidad de comensales de la reserva.", example = "4")
  private Integer comensales;

  // swagger
  @Schema(description = "Código numérico del estado de la reserva. 0=PENDIENTE, 1=CONFIRMADA, 2=CANCELADA, 3=NO_SHOW.", example = "0")
  private Integer estado;

  // swagger
  @Schema(description = "Descripción textual del estado de la reserva.", example = "PENDIENTE")
  private String estadoDescripcion;

  // swagger
  @Schema(description = "Códigos de las mesas asignadas a la reserva.", example = "[\"BS4\"]")
  private List<String> mesasAsignadas;
}