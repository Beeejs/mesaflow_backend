package com.mesaflow.mesaflow_api.Service;

import com.mesaflow.mesaflow_api.Model.Usuario;
import com.mesaflow.mesaflow_api.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

  private final UsuarioRepository usuarioRepository;

  public UsuarioService(
    UsuarioRepository usuarioRepository
  ) {
    this.usuarioRepository = usuarioRepository;
  }

  // Método para validar si un usuario existe y está activo
  public Usuario validarUsuarioActivo(Integer idUsuario) {
    // Evaluar si no envia usuario id
    if (idUsuario == null) {
      throw new RuntimeException("Debe indicar un usuario.");
    }

    // Verificar si el usuario existe y está activo
    return usuarioRepository
      .findByIdUsuarioAndActivoTrue(idUsuario)
      .orElseThrow(() -> new RuntimeException("El usuario no existe o no está activo."));
  }
}