package com.mesaflow.mesaflow_api.Repository;

import com.mesaflow.mesaflow_api.Model.Establecimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface EstablecimientoRepository
        extends JpaRepository<Establecimiento, Integer> {
}
