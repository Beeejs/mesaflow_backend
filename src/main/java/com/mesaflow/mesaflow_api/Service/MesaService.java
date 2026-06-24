package com.mesaflow.mesaflow_api.Service;

import com.mesaflow.mesaflow_api.Model.Mesa;
import com.mesaflow.mesaflow_api.Model.Reserva;
import com.mesaflow.mesaflow_api.Model.ReservaMesa;
import com.mesaflow.mesaflow_api.Repository.MesaRepository;
import com.mesaflow.mesaflow_api.Repository.ReservaMesaRepository;
import com.mesaflow.mesaflow_api.Repository.ReservaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MesaService {

  private final MesaRepository mesaRepository;
  private final ReservaRepository reservaRepository;
  private final ReservaMesaRepository reservaMesaRepository;

  public MesaService(
    MesaRepository mesaRepository,
    ReservaRepository reservaRepository,
    ReservaMesaRepository reservaMesaRepository
  ) {
    this.mesaRepository = mesaRepository;
    this.reservaRepository = reservaRepository;
    this.reservaMesaRepository = reservaMesaRepository;
  }

  // Método para buscar mesas disponibles para una reserva
  public List<Mesa> buscarMesasDisponiblesParaReserva(
    Integer idEstablecimiento,
    LocalDateTime fechaHoraInicio,
    LocalDateTime fechaHoraFin,
    Integer comensales,
    Boolean permiteCombinacionDinamica
  ) {
    List<Mesa> mesasLibres = obtenerMesasLibres(
      idEstablecimiento,
      fechaHoraInicio,
      fechaHoraFin
    );

    Optional<Mesa> mesaIndividual = buscarMesaIndividual(mesasLibres, comensales);

    // Si se encuentra una mesa individual, se devuelve una lista con esa mesa
    if (mesaIndividual.isPresent()) {
      return List.of(mesaIndividual.get());
    }

    // Si no se encuentra una mesa individual y se permite la combinación dinámica, se busca una combinación de mesas
    if (Boolean.TRUE.equals(permiteCombinacionDinamica)) {
      return buscarCombinacionDeMesas(mesasLibres, comensales);
    }

    // Si no se encuentra una mesa individual y no se permite la combinación dinámica, se devuelve una lista vacía
    return Collections.emptyList();
  }

  private List<Mesa> obtenerMesasLibres(
    Integer idEstablecimiento,
    LocalDateTime fechaHoraInicio,
    LocalDateTime fechaHoraFin
  ) {
    // Obtener mesas activas
    List<Mesa> mesasActivas = mesaRepository
      .findByEstablecimiento_IdEstablecimientoAndActivaTrueOrderByCapacidadAsc(idEstablecimiento);

    // Obtener reservas superpuestas
    List<Reserva> reservasSuperpuestas = reservaRepository.buscarReservasSuperpuestas(
      idEstablecimiento,
      fechaHoraInicio,
      fechaHoraFin
    );

    // Si no hay reservas superpuestas, se devuelve las mesas activas
    if (reservasSuperpuestas.isEmpty()) {
      return mesasActivas;
    }

    // Obtener IDs de reservas superpuestas
    List<Integer> idsReservas = reservasSuperpuestas.stream()
      .map(Reserva::getIdReserva)
      .collect(Collectors.toList());

    // Obtener mesas ocupadas
    List<ReservaMesa> reservasMesas = reservaMesaRepository.findByReserva_IdReservaIn(idsReservas);

    // Aramamos un conjunto de IDs de mesas ocupadas
    Set<Integer> idsMesasOcupadas = reservasMesas.stream()
      .map(reservaMesa -> reservaMesa.getMesa().getIdMesa())
      .collect(Collectors.toSet());

    // Devolver mesas libres, filtrando las mesas ocupadas
    return mesasActivas.stream()
      .filter(mesa -> !idsMesasOcupadas.contains(mesa.getIdMesa()))
      .collect(Collectors.toList());
  }

  // Método para buscar una mesa individual
  private Optional<Mesa> buscarMesaIndividual(List<Mesa> mesasLibres, Integer comensales) {
    return mesasLibres.stream()
      .filter(mesa -> mesa.getCapacidad() >= comensales)
      .min(Comparator.comparing(Mesa::getCapacidad));
  }

  // Método para buscar una combinación de mesas
  private List<Mesa> buscarCombinacionDeMesas(List<Mesa> mesasLibres, Integer comensales) {
    List<Mesa> mesasAgrupables = mesasLibres.stream()
      .filter(mesa -> Boolean.TRUE.equals(mesa.getAgrupable()))
      .sorted(Comparator.comparing(Mesa::getCapacidad))
      .collect(Collectors.toList());

    List<Mesa> combinacion = new ArrayList<>();
    int capacidadAcumulada = 0;

    for (Mesa mesa : mesasAgrupables) {
      combinacion.add(mesa);
      capacidadAcumulada += mesa.getCapacidad();

      if (capacidadAcumulada >= comensales) {
        return combinacion;
      }
    }

    return Collections.emptyList();
  }
}