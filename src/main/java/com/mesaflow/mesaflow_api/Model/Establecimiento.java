package com.mesaflow.mesaflow_api.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "establecimiento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Establecimiento {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_establecimiento")
  private Integer idEstablecimiento;

  @Column(name = "nombre", nullable = false)
  private String nombre;

  @Column(name = "tipo", nullable = false)
  private String tipo;

  @Column(name = "direccion", nullable = false)
  private String direccion;

  @Column(name = "telefono", nullable = false)
  private String telefono;

  @Column(name = "descripcion", nullable = false)
  private String descripcion;

  @Column(name = "permite_reserva", nullable = false)
  private Boolean permiteReserva;

  @Column(name = "activo", nullable = false)
  private Boolean activo;
}