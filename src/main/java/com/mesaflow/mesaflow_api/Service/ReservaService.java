package com.mesaflow.mesaflow_api.Service;

import com.mesaflow.mesaflow_api.DTOs.AccionReservaRequest;
import com.mesaflow.mesaflow_api.DTOs.AccionReservaResponse;
import com.mesaflow.mesaflow_api.DTOs.CrearReservaRequest;
import com.mesaflow.mesaflow_api.DTOs.ReservaResponse;
import com.mesaflow.mesaflow_api.Model.Establecimiento;
import com.mesaflow.mesaflow_api.Model.Mesa;
import com.mesaflow.mesaflow_api.Model.Reserva;
import com.mesaflow.mesaflow_api.Model.ReservaConfiguracion;
import com.mesaflow.mesaflow_api.Model.ReservaMesa;
import com.mesaflow.mesaflow_api.Model.Usuario;
import com.mesaflow.mesaflow_api.Repository.ReservaMesaRepository;
import com.mesaflow.mesaflow_api.Repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importante para manejar transacciones y evitar inconsistencias en la base de datos

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservaService {

  // Repositories
  private final ReservaRepository reservaRepository;
  private final ReservaMesaRepository reservaMesaRepository;

  // Services
  private final UsuarioService usuarioService;
  private final EstablecimientoService establecimientoService;
  private final ReservaConfiguracionService reservaConfiguracionService;
  private final MesaService mesaService;

  public ReservaService(
    ReservaRepository reservaRepository,
    ReservaMesaRepository reservaMesaRepository,
    UsuarioService usuarioService,
    EstablecimientoService establecimientoService,
    ReservaConfiguracionService reservaConfiguracionService,
    MesaService mesaService
  ) {
    this.reservaRepository = reservaRepository;
    this.reservaMesaRepository = reservaMesaRepository;
    this.usuarioService = usuarioService;
    this.establecimientoService = establecimientoService;
    this.reservaConfiguracionService = reservaConfiguracionService;
    this.mesaService = mesaService;
  }

  // Crear Reserva
  @Transactional // Asegura que todas las operaciones dentro del método se ejecuten como una sola transacción, evitando inconsistencias en caso de error (RESERVA, RESERVA_MESA)
  public ReservaResponse crearReserva(CrearReservaRequest request) {

    // Validaciones generales
    Usuario usuario = usuarioService.validarUsuarioActivo(request.getIdUsuario());

    Establecimiento establecimiento = establecimientoService
      .validarEstablecimientoParaReserva(request.getIdEstablecimiento());

    ReservaConfiguracion configuracion = reservaConfiguracionService
      .obtenerConfiguracionActiva(establecimiento.getIdEstablecimiento());

    // Calcular fecha/hora fin según configuración del establecimiento
    LocalDateTime fechaHoraFinCalculada = request.getFechaHoraInicio()
      .plusMinutes(configuracion.getDuracionReservaMinutos());

    // Validar que el usuario no tenga otra reserva activa en el mismo día
    validarUsuarioSinReservaActivaEnElDia(
      usuario.getIdUsuario(),
      request.getFechaHoraInicio()
    );

    // Validar que la nueva reserva no supere el porcentaje máximo de ocupación permitido
    reservaConfiguracionService.validarOcupacionMaxima(
      establecimiento.getIdEstablecimiento(),
      request.getFechaHoraInicio(),
      fechaHoraFinCalculada,
      request.getComensales(),
      configuracion
    );

    // Buscar mesas disponibles automáticamente
    List<Mesa> mesasAsignadas = mesaService.buscarMesasDisponiblesParaReserva(
      establecimiento.getIdEstablecimiento(),
      request.getFechaHoraInicio(),
      fechaHoraFinCalculada,
      request.getComensales(),
      configuracion.getPermiteCombinacionDinamica()
    );

    // Si no encontró mesa individual ni combinación válida, no se crea la reserva
    if (mesasAsignadas.isEmpty()) {
      throw new RuntimeException("No hay mesas disponibles para la fecha, hora y cantidad de comensales solicitados.");
    }

    // Crear reserva
    Reserva reserva = new Reserva();
    reserva.setUsuario(usuario);
    reserva.setEstablecimiento(establecimiento);
    reserva.setFechaHoraInicio(request.getFechaHoraInicio());
    reserva.setFechaHoraFinCalculada(fechaHoraFinCalculada);
    reserva.setComensales(request.getComensales());
    reserva.setEstado(0); // 0 = PENDIENTE | Por defautl la reserva es pendiente. El admin debe confirmarla
    reserva.setFechaCreacion(LocalDateTime.now());

    // Guardar reserva
    Reserva reservaGuardada = reservaRepository.save(reserva);

    // Guardar relación reserva_mesa
    for (int i = 0; i < mesasAsignadas.size(); i++) {
      ReservaMesa reservaMesa = new ReservaMesa();
      reservaMesa.setReserva(reservaGuardada);
      reservaMesa.setMesa(mesasAsignadas.get(i));
      reservaMesa.setMesaPrincipal(i == 0);

      reservaMesaRepository.save(reservaMesa);
    }

    // Armar lista de códigos de mesas asignadas para la respuesta
    List<String> codigosMesasAsignadas = mesasAsignadas.stream()
      .map(Mesa::getCodigo)
      .toList();

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
      codigosMesasAsignadas
    );
  }

  // Metodo privado para obtener la descripción del estado de la reserva
  private String obtenerDescripcionEstado(Integer estado) {
    if (estado == 0) return "PENDIENTE";
    if (estado == 1) return "CONFIRMADA";
    if (estado == 2) return "CANCELADA";
    if (estado == 3) return "NO_SHOW";
    return "DESCONOCIDO";
  }

  // Metodo para validar que el usuario no tenga otra reserva activa en el mismo día
  private void validarUsuarioSinReservaActivaEnElDia(
    Integer idUsuario,
    LocalDateTime fechaHoraInicio
  ) {
    LocalDateTime inicioDia = fechaHoraInicio.toLocalDate().atStartOfDay();
    LocalDateTime finDia = inicioDia.plusDays(1);

    Long cantidadReservasActivas = reservaRepository.contarReservasActivasDelUsuarioEnElDia(
      idUsuario,
      inicioDia,
      finDia
    );

    if (cantidadReservasActivas > 0) {
      throw new RuntimeException("El usuario ya tiene una reserva activa para ese día.");
    }
  }

  // Metodo para confirmar una reserva (solo para administradores del establecimiento)
  @Transactional // Asegura que todas las operaciones dentro del método se ejecuten como una sola transacción, evitando inconsistencias en la base de datos
  // Lo ponemos en publico para que aplique el @Transactional y no de error de proxy
  public AccionReservaResponse confirmarReserva(Integer idReserva, Integer idUsuario) {
    // Validar que la reserva exista
    Reserva reserva = reservaRepository.findById(idReserva)
      .orElseThrow(() -> new RuntimeException("La reserva no existe."));

    // Validar que el usuario sea administrador del establecimiento de la reserva
    usuarioService.validarAdminDelEstablecimiento(
      idUsuario,
      reserva.getEstablecimiento().getIdEstablecimiento()
    );

    // Validar que la reserva sea pendiente
    if (reserva.getEstado() != 0) {
      throw new RuntimeException("Solo se pueden confirmar reservas pendientes.");
    }

    // Cambiar estado de la reserva a confirmada
    reserva.setEstado(1); // 1 = CONFIRMADA

    // Actualizar reserva
    Reserva reservaGuardada = reservaRepository.save(reserva);

    return new AccionReservaResponse(
      reservaGuardada.getIdReserva(),
      reservaGuardada.getEstado(),
      obtenerDescripcionEstado(reservaGuardada.getEstado())
    );
  }

  // Metodo para ejecutar acciones sobre una reserva (confirmar, cancelar)
  public AccionReservaResponse ejecutarAccionReserva(
    Integer idReserva,
    AccionReservaRequest request
  ) {
    String scope = request.getScope().trim().toUpperCase();

    if ("CONFIRMAR".equals(scope)) {
      return confirmarReserva(idReserva, request.getIdUsuario());
    }

    throw new RuntimeException("La acción indicada no es válida.");
  }
}