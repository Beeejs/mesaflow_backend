package com.mesaflow.mesaflow_api.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "reserva")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_reserva")
  private Integer idReserva;

  @Column(name = "fecha_hora_inicio", nullable = false)
  private LocalDateTime fechaHoraInicio;

  @Column(name = "fecha_hora_fin_calculada", nullable = false)
  private LocalDateTime fechaHoraFinCalculada;

  @Column(name = "comensales", nullable = false)
  private Integer comensales;

  /*
  Estados:
  0 = PENDIENTE
  1 = CONFIRMADA
  2 = CANCELADA
  3 = FINALIZADA
  4 = NO_SHOW
  */
  @Column(name = "estado", nullable = false)
  private Integer estado;

  @Column(name = "fecha_creacion", nullable = false)
  private LocalDateTime fechaCreacion;

  @ManyToOne
  @JoinColumn(name = "id_usuario", nullable = false)
  private Usuario usuario;

  @ManyToOne
  @JoinColumn(name = "id_establecimiento", nullable = false)
  private Establecimiento establecimiento;
}