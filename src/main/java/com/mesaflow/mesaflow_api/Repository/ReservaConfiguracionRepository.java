package com.mesaflow.mesaflow_api.Repository;

import com.mesaflow.mesaflow_api.Model.ReservaConfiguracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservaConfiguracionRepository extends JpaRepository<ReservaConfiguracion, Integer> {

  Optional<ReservaConfiguracion> findByEstablecimiento_IdEstablecimientoAndActivoTrue(Integer idEstablecimiento);
}