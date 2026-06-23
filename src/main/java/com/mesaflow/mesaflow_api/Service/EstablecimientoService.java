package com.mesaflow.mesaflow_api.Service;

import com.mesaflow.mesaflow_api.Model.Establecimiento;
import com.mesaflow.mesaflow_api.Repository.EstablecimientoRepository;
import org.springframework.stereotype.Service;

@Service
public class EstablecimientoService {

  private final EstablecimientoRepository establecimientoRepository;

  public EstablecimientoService(
    EstablecimientoRepository establecimientoRepository
  ) {
    this.establecimientoRepository = establecimientoRepository;
  }

  // Método para validar si un establecimiento existe, está activo y permite reservas
  public Establecimiento validarEstablecimientoParaReserva(Integer idEstablecimiento) {
    // Verificar si el establecimiento existe y está activo
    Establecimiento establecimiento = establecimientoRepository
      .findByIdEstablecimientoAndActivoTrue(idEstablecimiento)
      .orElseThrow(() -> new RuntimeException("El establecimiento no existe o no está activo."));

    // Verificar si el establecimiento permite reservas
    if (!Boolean.TRUE.equals(establecimiento.getPermiteReserva())) {
      throw new RuntimeException("El establecimiento no permite reservas.");
    }

    // Devolvemos el establecimiento
    return establecimiento;
  }
}