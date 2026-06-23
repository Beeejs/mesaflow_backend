package com.mesaflow.mesaflow_api.Controller;

import com.mesaflow.mesaflow_api.Service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @GetMapping("/validar/{id}")
    public String validarReserva(@PathVariable Integer id) {

        try {

            reservaService.validarEstablecimiento(id);

            return "Reserva válida";

        } catch (Exception e) {

            return e.getMessage();

        }
    }
}
