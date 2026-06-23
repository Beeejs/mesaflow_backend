package com.mesaflow.mesaflow_api.Repository;

import com.mesaflow.mesaflow_api.Model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MesaRepository extends JpaRepository<Mesa, Integer> {

  List<Mesa> findByEstablecimiento_IdEstablecimientoAndActivaTrue(Integer idEstablecimiento);

  List<Mesa> findByEstablecimiento_IdEstablecimientoAndActivaTrueOrderByCapacidadAsc(Integer idEstablecimiento);

  List<Mesa> findByEstablecimiento_IdEstablecimientoAndActivaTrueAndAgrupableTrueOrderByCapacidadAsc(Integer idEstablecimiento);

  @Query("""
    SELECT COALESCE(SUM(m.capacidad), 0)
    FROM Mesa m
    WHERE m.establecimiento.idEstablecimiento = :idEstablecimiento
    AND m.activa = true
  """)
  Integer calcularCapacidadTotalActiva(
    @Param("idEstablecimiento") Integer idEstablecimiento
  );
}