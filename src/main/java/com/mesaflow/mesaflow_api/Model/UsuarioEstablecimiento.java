package com.mesaflow.mesaflow_api.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuario_establecimiento")
@Getter
@Setter
public class UsuarioEstablecimiento {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_usuario_establecimiento")
  private Integer idUsuarioEstablecimiento;

  @ManyToOne
  @JoinColumn(name = "id_usuario")
  private Usuario usuario;

  @ManyToOne
  @JoinColumn(name = "id_establecimiento")
  private Establecimiento establecimiento;

  @Column(name = "rol_establecimiento")
  private String rolEstablecimiento;

  private Boolean activo;
}