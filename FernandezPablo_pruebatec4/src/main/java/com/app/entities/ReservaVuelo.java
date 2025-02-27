package com.app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ReservaVuelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vuelo_id", nullable = false)
    private Vuelo vuelo;

    private String origen;
    private String destino;

    private LocalDate fechaIda;

    private int cantidadPersonas;

    @ManyToMany
    @JoinTable(
            name = "reserva_vuelo_usuario",
            joinColumns = @JoinColumn(name = "reserva_vuelo_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> pasajeros;

    private double montoTotal;
}