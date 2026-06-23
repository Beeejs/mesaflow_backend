package com.mesaflow.mesaflow_api.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DisponibilidadReservaResponse {

  private Boolean disponible;

  private String mensaje;

  private Integer idEstablecimiento;

  private LocalDateTime fechaHoraInicio;

  private LocalDateTime fechaHoraFinCalculada;

  private Integer comensalesSolicitados;

  private Integer capacidadTotal;

  private Integer comensalesYaReservados;

  private BigDecimal porcentajeOcupacionMaxima;

  private List<String> mesasDisponibles;
}