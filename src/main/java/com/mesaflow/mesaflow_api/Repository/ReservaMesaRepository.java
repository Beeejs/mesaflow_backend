package com.mesaflow.mesaflow_api.Repository;

import com.mesaflow.mesaflow_api.Model.ReservaMesa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaMesaRepository extends JpaRepository<ReservaMesa, Integer> {

  List<ReservaMesa> findByReserva_IdReserva(Integer idReserva);

  List<ReservaMesa> findByReserva_IdReservaIn(List<Integer> idsReservas);

  List<ReservaMesa> findByMesa_IdMesa(Integer idMesa);
}