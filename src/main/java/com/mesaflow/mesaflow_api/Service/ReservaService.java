package com.mesaflow.mesaflow_api.Service;

import com.mesaflow.mesaflow_api.DTOs.CrearReservaRequest;
import com.mesaflow.mesaflow_api.DTOs.ReservaResponse;
import com.mesaflow.mesaflow_api.Model.Establecimiento;
import com.mesaflow.mesaflow_api.Model.Reserva;
import com.mesaflow.mesaflow_api.Model.ReservaConfiguracion;
import com.mesaflow.mesaflow_api.Model.Usuario;
import com.mesaflow.mesaflow_api.Repository.ReservaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReservaService {

  // Repositories
  private final ReservaRepository reservaRepository;
  // Services
  private final UsuarioService usuarioService;
  private final EstablecimientoService establecimientoService;
  private final ReservaConfiguracionService reservaConfiguracionService;

  public ReservaService(
    ReservaRepository reservaRepository,
    UsuarioService usuarioService,
    EstablecimientoService establecimientoService,
    ReservaConfiguracionService reservaConfiguracionService
  ) {
    this.reservaRepository = reservaRepository;
    this.usuarioService = usuarioService;
    this.establecimientoService = establecimientoService;
    this.reservaConfiguracionService = reservaConfiguracionService;
  }

  // Crear Reserva
  public ReservaResponse crearReserva(CrearReservaRequest request) {

    // Validaciones
    Usuario usuario = usuarioService.validarUsuarioActivo(request.getIdUsuario());

    Establecimiento establecimiento = establecimientoService
      .validarEstablecimientoParaReserva(request.getIdEstablecimiento());

    ReservaConfiguracion configuracion = reservaConfiguracionService
      .obtenerConfiguracionActiva(establecimiento.getIdEstablecimiento());
      
    LocalDateTime fechaHoraFinCalculada = request.getFechaHoraInicio()
      .plusMinutes(configuracion.getDuracionReservaMinutos());

    // Creacion de la reserva
    Reserva reserva = new Reserva();
    reserva.setUsuario(usuario);
    reserva.setEstablecimiento(establecimiento);
    reserva.setFechaHoraInicio(request.getFechaHoraInicio());
    reserva.setFechaHoraFinCalculada(fechaHoraFinCalculada);
    reserva.setComensales(request.getComensales());
    reserva.setEstado(1); // 1 = CONFIRMADA
    reserva.setFechaCreacion(LocalDateTime.now());

    // Guardamos en la base de datos
    Reserva reservaGuardada = reservaRepository.save(reserva);

    return new ReservaResponse(
      reservaGuardada.getIdReserva(),
      usuario.getIdUsuario(),
      usuario.getNombre() + " " + usuario.getApellido(),
      establecimiento.getIdEstablecimiento(),
      establecimiento.getNombre(),
      reservaGuardada.getFechaHoraInicio(),
      reservaGuardada.getFechaHoraFinCalculada(),
      reservaGuardada.getComensales(),
      reservaGuardada.getEstado(),
      obtenerDescripcionEstado(reservaGuardada.getEstado()),
      null
    );
  }


  private String obtenerDescripcionEstado(Integer estado) {
    if (estado == 0) return "PENDIENTE";
    if (estado == 1) return "CONFIRMADA";
    if (estado == 2) return "CANCELADA";
    if (estado == 3) return "FINALIZADA";
    if (estado == 4) return "NO_SHOW";
    return "DESCONOCIDO";
  }
}