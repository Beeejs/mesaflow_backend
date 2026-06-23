package com.mesaflow.mesaflow_api.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ReservaResponse {

  private Integer idReserva;

  private Integer idUsuario;

  private String nombreUsuario;

  private Integer idEstablecimiento;

  private String nombreEstablecimiento;

  private LocalDateTime fechaHoraInicio;

  private LocalDateTime fechaHoraFinCalculada;

  private Integer comensales;

  private Integer estado;

  private String estadoDescripcion;

  private List<String> mesasAsignadas;
}