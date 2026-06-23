package com.mesaflow.mesaflow_api.Repository;

import com.mesaflow.mesaflow_api.Model.Establecimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstablecimientoRepository extends JpaRepository<Establecimiento, Integer> {

  List<Establecimiento> findByActivoTrue();

  List<Establecimiento> findByActivoTrueAndPermiteReservaTrue();

  Optional<Establecimiento> findByIdEstablecimientoAndActivoTrue(Integer idEstablecimiento);

  Optional<Establecimiento> findByIdEstablecimientoAndActivoTrueAndPermiteReservaTrue(Integer idEstablecimiento);
}