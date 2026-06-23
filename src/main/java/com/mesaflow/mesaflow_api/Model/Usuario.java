package com.mesaflow.mesaflow_api.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_usuario")
  private Integer idUsuario;

  @Column(name = "nombre", nullable = false)
  private String nombre;

  @Column(name = "apellido", nullable = false)
  private String apellido;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "rol", nullable = false)
  private String rol;

  @Column(name = "activo", nullable = false)
  private Boolean activo;

  @Column(name = "fecha_creacion", nullable = false)
  private LocalDateTime fechaCreacion;
}