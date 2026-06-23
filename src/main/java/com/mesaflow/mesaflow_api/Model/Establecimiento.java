package com.mesaflow.mesaflow_api.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "establecimiento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Establecimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEstablecimiento;

    private String nombre;

    private String tipo;

    private Boolean permiteReserva;

    private Boolean activo;
}

