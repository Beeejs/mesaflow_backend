package com.mesaflow.mesaflow_api.Service;

import com.mesaflow.mesaflow_api.Model.Usuario;
import com.mesaflow.mesaflow_api.Repository.UsuarioEstablecimientoRepository;
import com.mesaflow.mesaflow_api.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

  private final UsuarioRepository usuarioRepository;
  private final UsuarioEstablecimientoRepository usuarioEstablecimientoRepository;

  public UsuarioService(
    UsuarioRepository usuarioRepository,
    UsuarioEstablecimientoRepository usuarioEstablecimientoRepository
  ) {
    this.usuarioRepository = usuarioRepository;
    this.usuarioEstablecimientoRepository = usuarioEstablecimientoRepository;
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

  // Método para validar si un usuario es administrador de un establecimiento
  public Usuario validarAdminDelEstablecimiento(
    Integer idUsuario,
    Integer idEstablecimiento
  ) {
    // Validar que el usuario este activo
    Usuario usuario = validarUsuarioActivo(idUsuario);

    // Verificar si el usuario tiene el rol de administrador del establecimiento
    if (!"ADMIN_ESTABLECIMIENTO".equals(usuario.getRol())) {
      throw new RuntimeException("El usuario no tiene permisos para confirmar reservas.");
    }

    // Verificar si el usuario es administrador del establecimiento
    boolean esAdminDelEstablecimiento = usuarioEstablecimientoRepository
      .existsByUsuario_IdUsuarioAndEstablecimiento_IdEstablecimientoAndRolEstablecimientoAndActivoTrue(
        idUsuario,
        idEstablecimiento,
        "ADMIN"
      );
    
    // Si no es administrador del establecimiento, lanzar excepción
    if (!esAdminDelEstablecimiento) {
      throw new RuntimeException("El usuario no es administrador del establecimiento de la reserva.");
    }

    return usuario;
  }
}