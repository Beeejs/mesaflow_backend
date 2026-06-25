package com.mesaflow.mesaflow_api.Service;

import com.mesaflow.mesaflow_api.Model.Reserva;
import com.mesaflow.mesaflow_api.Model.ReservaConfiguracion;
import com.mesaflow.mesaflow_api.Repository.MesaRepository;
import com.mesaflow.mesaflow_api.Repository.ReservaConfiguracionRepository;
import com.mesaflow.mesaflow_api.Repository.ReservaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
public class ReservaConfiguracionService {

  private final ReservaConfiguracionRepository reservaConfiguracionRepository;
  private final MesaRepository mesaRepository;
  private final ReservaRepository reservaRepository;

  public ReservaConfiguracionService(
    ReservaConfiguracionRepository reservaConfiguracionRepository,
    MesaRepository mesaRepository,
    ReservaRepository reservaRepository
  ) {
    this.reservaConfiguracionRepository = reservaConfiguracionRepository;
    this.mesaRepository = mesaRepository;
    this.reservaRepository = reservaRepository;
  }

  // Método para obtener la configuración activa de reservas de un establecimiento
  public ReservaConfiguracion obtenerConfiguracionActiva(Integer idEstablecimiento) {
    // Evaluar si no envía establecimiento id
    if (idEstablecimiento == null) {
      throw new RuntimeException("Debe indicar un establecimiento.");
    }

    // Verificar si el establecimiento tiene una configuración de reservas activa
    return reservaConfiguracionRepository
      .findByEstablecimiento_IdEstablecimientoAndActivoTrue(idEstablecimiento)
      .orElseThrow(() -> new RuntimeException("El establecimiento no tiene configuración de reservas activa."));
  }

  // Método para validar que la nueva reserva no supere el porcentaje máximo de ocupación
  public void validarOcupacionMaxima(
    Integer idEstablecimiento,
    LocalDateTime fechaHoraInicio,
    LocalDateTime fechaHoraFin,
    Integer comensalesNuevaReserva,
    ReservaConfiguracion configuracion
  ) {
    // Obtener la capacidad total de mesas activas del establecimiento
    Integer capacidadTotal = mesaRepository.calcularCapacidadTotalActiva(idEstablecimiento);

    // Si la capacidad total es nula o menor o igual a cero, lanzar excepción
    if (capacidadTotal == null || capacidadTotal <= 0) {
      throw new RuntimeException("El establecimiento no tiene mesas activas configuradas.");
    }

    // Calcular la cantidad de comensales ya reservados en el mismo rango de tiempo
    Integer comensalesYaReservados = reservaRepository.calcularComensalesReservadosSuperpuestos(
      idEstablecimiento,
      fechaHoraInicio,
      fechaHoraFin
    );

    // Si la cantidad de comensales ya reservados es nula, se considera como cero
    if (comensalesYaReservados == null) {
      comensalesYaReservados = 0;
    }

    // Validar que la ocupación final no supere el porcentaje máximo permitido
    BigDecimal porcentajeMaximo = configuracion.getPorcentajeOcupacionMaxima();

    // Si el porcentaje máximo es nulo, lanzar excepción
    if (porcentajeMaximo == null) {
      throw new RuntimeException("El establecimiento no tiene configurado el porcentaje máximo de ocupación.");
    }
    
    // Calcular la ocupación final y compararla con el porcentaje máximo permitido
    BigDecimal capacidadMaximaPermitidaDecimal = BigDecimal.valueOf(capacidadTotal)
      .multiply(porcentajeMaximo)
      .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

    // Redondear hacia abajo para obtener un número entero de comensales permitidos 
    int capacidadMaximaPermitida = capacidadMaximaPermitidaDecimal
      .setScale(0, RoundingMode.FLOOR)
      .intValue();

    // Calcular la ocupación final sumando los comensales ya reservados y los de la nueva reserva
    int ocupacionFinal = comensalesYaReservados + comensalesNuevaReserva;

    // Validar si la ocupación final supera la capacidad máxima permitida
    if (ocupacionFinal > capacidadMaximaPermitida) {
      throw new RuntimeException(
        "La reserva supera el porcentaje máximo de ocupación permitido para el establecimiento."
      );
    }

    /*
    Entendimiento de logica:

    Ejemplo:

    capacidadTotal = 30
    porcentajeMaximo = 80
    comensalesYaReservados = 22
    comensalesNuevaReserva = 3

    Calcula:

    capacidadMaximaPermitida = 30 * 80 / 100 = 24
    ocupacionFinal = 22 + 3 = 25

    Como:

    25 > 24

    entonces rechaza la reserva.
    */
  }

  // Metodo para validar el tiempo límite de cancelación
  public void validarCancelacionPermitida(
    Reserva reserva,
    ReservaConfiguracion configuracion
  ) {
    // Validar si el establecimiento permite cancelaciones
    if (!Boolean.TRUE.equals(configuracion.getPermiteCancelacion())) {
      throw new RuntimeException("El establecimiento no permite cancelar reservas.");
    }

    // Obtenemos el tiempo límite de cancelación en minutos desde la configuración
    Integer tiempoLimiteCancelacionMinutos = configuracion.getTiempoLimiteCancelacionMinutos();

    // Si el tiempo límite de cancelación es nulo, lanzar excepción
    if (tiempoLimiteCancelacionMinutos == null) {
      throw new RuntimeException("El establecimiento no tiene configurado el tiempo límite de cancelación.");
    }

    // Obtenemos la fecha y hora de inicio de la reserva
    LocalDateTime fechaHoraLimiteCancelacion = reserva.getFechaHoraInicio()
      .minusMinutes(tiempoLimiteCancelacionMinutos);

    // Obtenemos la fecha y hora actual
    LocalDateTime ahora = LocalDateTime.now();

    // Validar si la reserva ya superó el tiempo límite de cancelación
    if (ahora.isAfter(fechaHoraLimiteCancelacion)) {
      throw new RuntimeException("La reserva ya no puede cancelarse porque superó el tiempo límite de cancelación.");
    }
  }
}