package com.mesaflow.mesaflow_api.Controller;

import com.mesaflow.mesaflow_api.DTOs.CrearReservaRequest;
import com.mesaflow.mesaflow_api.DTOs.ReservaResponse;
import com.mesaflow.mesaflow_api.Service.ReservaService;
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

    @PostMapping
    public ResponseEntity<ReservaResponse> crearReserva(@RequestBody CrearReservaRequest request) {
        return ResponseEntity.ok(reservaService.crearReserva(request));
    }
}