package com.mesaflow.mesaflow_api.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ConsultarDisponibilidadRequest {

  private Integer idEstablecimiento;

  private LocalDateTime fechaHoraInicio;

  private Integer comensales;
}