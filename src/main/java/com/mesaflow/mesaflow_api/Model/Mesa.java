package com.mesaflow.mesaflow_api.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "mesa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mesa {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_mesa")
  private Integer idMesa;

  @Column(name = "codigo", nullable = false)
  private String codigo;

  @Column(name = "capacidad", nullable = false)
  private Integer capacidad;

  @Column(name = "agrupable", nullable = false)
  private Boolean agrupable;

  @Column(name = "activa", nullable = false)
  private Boolean activa;

  @ManyToOne
  @JoinColumn(name = "id_establecimiento", nullable = false)
  private Establecimiento establecimiento;
}