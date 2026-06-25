package com.mesaflow.mesaflow_api.Controller;

import com.mesaflow.mesaflow_api.DTOs.AccionReservaRequest;
import com.mesaflow.mesaflow_api.DTOs.AccionReservaResponse;
import com.mesaflow.mesaflow_api.DTOs.ApiResponse;
import com.mesaflow.mesaflow_api.DTOs.CrearReservaRequest;
import com.mesaflow.mesaflow_api.DTOs.ReservaResponse;
import com.mesaflow.mesaflow_api.Service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

  private final ReservaService reservaService;

  public ReservaController(ReservaService reservaService) {
    this.reservaService = reservaService;
  }

  // Crear Reserva
  @PostMapping
  public ResponseEntity<ApiResponse<ReservaResponse>> crearReserva(
    @Valid @RequestBody CrearReservaRequest request
  ) {
    ReservaResponse reservaCreada = reservaService.crearReserva(request);

    return ResponseEntity.ok(ApiResponse.ok(reservaCreada));
  }

  // Ejecutar acción sobre una reserva (confirmar, cancelar)
  @PutMapping("/{idReserva}/accion")
  public ResponseEntity<ApiResponse<AccionReservaResponse>> ejecutarAccionReserva(
      @PathVariable Integer idReserva,
      @Valid @RequestBody AccionReservaRequest request
  ) {
    AccionReservaResponse reservaActualizada = reservaService.ejecutarAccionReserva(
      idReserva,
      request
    );

    return ResponseEntity.ok(ApiResponse.ok(reservaActualizada));
  }
}