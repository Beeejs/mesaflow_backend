package com.mesaflow.mesaflow_api.Service;

import com.mesaflow.mesaflow_api.DTOs.CrearReservaRequest;
import com.mesaflow.mesaflow_api.DTOs.ReservaResponse;
import com.mesaflow.mesaflow_api.Model.Establecimiento;
import com.mesaflow.mesaflow_api.Model.Reserva;
import com.mesaflow.mesaflow_api.Model.ReservaConfiguracion;
import com.mesaflow.mesaflow_api.Model.Usuario;
import com.mesaflow.mesaflow_api.Repository.EstablecimientoRepository;
import com.mesaflow.mesaflow_api.Repository.ReservaConfiguracionRepository;
import com.mesaflow.mesaflow_api.Repository.ReservaRepository;
import com.mesaflow.mesaflow_api.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReservaService {

    private final UsuarioRepository usuarioRepository;
    private final EstablecimientoRepository establecimientoRepository;
    private final ReservaConfiguracionRepository reservaConfiguracionRepository;
    private final ReservaRepository reservaRepository;

    public ReservaService(
      UsuarioRepository usuarioRepository,
      EstablecimientoRepository establecimientoRepository,
      ReservaConfiguracionRepository reservaConfiguracionRepository,
      ReservaRepository reservaRepository
    ) {
      this.usuarioRepository = usuarioRepository;
      this.establecimientoRepository = establecimientoRepository;
      this.reservaConfiguracionRepository = reservaConfiguracionRepository;
      this.reservaRepository = reservaRepository;
    }

    // Crear Reserva
    public ReservaResponse crearReserva(CrearReservaRequest request) {

        Usuario usuario = usuarioRepository.findByIdUsuarioAndActivoTrue(request.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("El usuario no existe o no está activo."));

        Establecimiento establecimiento = establecimientoRepository
                .findByIdEstablecimientoAndActivoTrue(request.getIdEstablecimiento())
                .orElseThrow(() -> new RuntimeException("El establecimiento no existe o no está activo."));

        ReservaConfiguracion configuracion = reservaConfiguracionRepository
                .findByEstablecimiento_IdEstablecimientoAndActivoTrue(establecimiento.getIdEstablecimiento())
                .orElseThrow(() -> new RuntimeException("El establecimiento no tiene configuración de reservas activa."));

        LocalDateTime fechaHoraFinCalculada = request.getFechaHoraInicio()
                .plusMinutes(configuracion.getDuracionReservaMinutos());

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setEstablecimiento(establecimiento);
        reserva.setFechaHoraInicio(request.getFechaHoraInicio());
        reserva.setFechaHoraFinCalculada(fechaHoraFinCalculada);
        reserva.setComensales(request.getComensales());
        reserva.setEstado(1); // 1 = CONFIRMADA
        reserva.setFechaCreacion(LocalDateTime.now());

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