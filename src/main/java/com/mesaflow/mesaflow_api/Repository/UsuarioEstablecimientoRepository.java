package com.mesaflow.mesaflow_api.Repository;

import com.mesaflow.mesaflow_api.Model.UsuarioEstablecimiento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioEstablecimientoRepository extends JpaRepository<UsuarioEstablecimiento, Integer> {

  //Que hace > ¿Existe una relación activa donde este usuario sea ADMIN de este establecimiento?
  boolean existsByUsuario_IdUsuarioAndEstablecimiento_IdEstablecimientoAndRolEstablecimientoAndActivoTrue(
    Integer idUsuario,
    Integer idEstablecimiento,
    String rolEstablecimiento
  );
}