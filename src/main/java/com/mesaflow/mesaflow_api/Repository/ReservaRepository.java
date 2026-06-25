package com.mesaflow.mesaflow_api.Repository;

import com.mesaflow.mesaflow_api.Model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    List<Reserva> findByUsuario_IdUsuarioOrderByFechaHoraInicioDesc(Integer idUsuario);

    List<Reserva> findByEstablecimiento_IdEstablecimientoOrderByFechaHoraInicioDesc(Integer idEstablecimiento);

    List<Reserva> findByEstablecimiento_IdEstablecimientoAndFechaHoraInicioBetweenOrderByFechaHoraInicioAsc(
      Integer idEstablecimiento,
      LocalDateTime fechaDesde,
      LocalDateTime fechaHasta
    );

    @Query("""
      SELECT r
      FROM Reserva r
      WHERE r.establecimiento.idEstablecimiento = :idEstablecimiento
      AND r.estado IN (0, 1)
      AND r.fechaHoraInicio < :fechaHoraFin
      AND r.fechaHoraFinCalculada > :fechaHoraInicio
    """)
    List<Reserva> buscarReservasSuperpuestas(
      @Param("idEstablecimiento") Integer idEstablecimiento,
      @Param("fechaHoraInicio") LocalDateTime fechaHoraInicio,
      @Param("fechaHoraFin") LocalDateTime fechaHoraFin
    );

    @Query("""
      SELECT COALESCE(SUM(r.comensales), 0)
      FROM Reserva r
      WHERE r.establecimiento.idEstablecimiento = :idEstablecimiento
      AND r.estado IN (0, 1)
      AND r.fechaHoraInicio < :fechaHoraFin
      AND r.fechaHoraFinCalculada > :fechaHoraInicio
    """)
    Integer calcularComensalesReservadosSuperpuestos(
      @Param("idEstablecimiento") Integer idEstablecimiento,
      @Param("fechaHoraInicio") LocalDateTime fechaHoraInicio,
      @Param("fechaHoraFin") LocalDateTime fechaHoraFin
    );

    @Query("""
      SELECT COUNT(r)
      FROM Reserva r
      WHERE r.usuario.idUsuario = :idUsuario
      AND r.estado IN (0, 1)
      AND r.fechaHoraInicio >= :inicioDia
      AND r.fechaHoraInicio < :finDia
    """)
    Long contarReservasActivasDelUsuarioEnElDia(
      @Param("idUsuario") Integer idUsuario,
      @Param("inicioDia") LocalDateTime inicioDia,
      @Param("finDia") LocalDateTime finDia
    );
}