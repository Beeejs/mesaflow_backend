package com.mesaflow.mesaflow_api.Service;

import com.mesaflow.mesaflow_api.Model.Establecimiento;
import com.mesaflow.mesaflow_api.Repository.EstablecimientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ReservaService {

    @Autowired
    private EstablecimientoRepository establecimientoRepository;

    public void validarEstablecimiento(Integer idEstablecimiento) {

        Establecimiento establecimiento =
                establecimientoRepository.findById(idEstablecimiento)
                        .orElseThrow();

        if (!establecimiento.getActivo()) {
            throw new RuntimeException("Establecimiento inactivo");
        }

        if (!establecimiento.getPermiteReserva()) {
            throw new RuntimeException("El establecimiento no permite reservas");
        }
    }
}
