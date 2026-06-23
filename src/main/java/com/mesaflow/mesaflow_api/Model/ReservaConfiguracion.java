package com.mesaflow.mesaflow_api.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "reserva_configuracion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservaConfiguracion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_reserva_configuracion")
  private Integer idReservaConfiguracion;

  @Column(name = "duracion_reserva_minutos", nullable = false)
  private Integer duracionReservaMinutos;

  @Column(name = "tiempo_tolerancia_minutos", nullable = false)
  private Integer tiempoToleranciaMinutos;

  @Column(name = "anticipacion_minima_minutos", nullable = false)
  private Integer anticipacionMinimaMinutos;

  @Column(name = "anticipacion_maxima_dias", nullable = false)
  private Integer anticipacionMaximaDias;

  @Column(name = "permite_cancelacion", nullable = false)
  private Boolean permiteCancelacion;

  @Column(name = "tiempo_limite_cancelacion_minutos", nullable = false)
  private Integer tiempoLimiteCancelacionMinutos;

  @Column(name = "activo", nullable = false)
  private Boolean activo;

  @Column(name = "porcentaje_ocupacion_maxima", nullable = false)
  private BigDecimal porcentajeOcupacionMaxima;

  @Column(name = "permite_combinacion_dinamica", nullable = false)
  private Boolean permiteCombinacionDinamica;

  @ManyToOne
  @JoinColumn(name = "id_establecimiento", nullable = false)
  private Establecimiento establecimiento;
}