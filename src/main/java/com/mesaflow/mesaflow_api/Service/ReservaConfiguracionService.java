package com.mesaflow.mesaflow_api.Service;

import com.mesaflow.mesaflow_api.Model.ReservaConfiguracion;
import com.mesaflow.mesaflow_api.Repository.ReservaConfiguracionRepository;
import org.springframework.stereotype.Service;

@Service
public class ReservaConfiguracionService {

  private final ReservaConfiguracionRepository reservaConfiguracionRepository;

  public ReservaConfiguracionService(
    ReservaConfiguracionRepository reservaConfiguracionRepository
  ) {
    this.reservaConfiguracionRepository = reservaConfiguracionRepository;
  }

  // Método para obtener la configuración activa de reservas de un establecimiento
  public ReservaConfiguracion obtenerConfiguracionActiva(Integer idEstablecimiento) {
    // Evaluar si no envia establecimiento id
    if (idEstablecimiento == null) {
      throw new RuntimeException("Debe indicar un establecimiento.");
    }

    // Verificar si el establecimiento tiene una configuración de reservas activa
    return reservaConfiguracionRepository
      .findByEstablecimiento_IdEstablecimientoAndActivoTrue(idEstablecimiento)
      .orElseThrow(() -> new RuntimeException("El establecimiento no tiene configuración de reservas activa."));
  }
}