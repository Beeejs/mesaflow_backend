package com.mesaflow.mesaflow_api.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reserva_mesa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservaMesa {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_reserva_mesa")
  private Integer idReservaMesa;

  @ManyToOne
  @JoinColumn(name = "id_reserva", nullable = false)
  private Reserva reserva;

  @ManyToOne
  @JoinColumn(name = "id_mesa", nullable = false)
  private Mesa mesa;

  @Column(name = "mesa_principal", nullable = false)
  private Boolean mesaPrincipal;
}