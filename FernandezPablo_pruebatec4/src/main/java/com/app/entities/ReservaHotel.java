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
public class ReservaHotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    private int cantidadPersonas;

    @ManyToMany
    @JoinTable(
            name = "reserva_hotel_usuario",
            joinColumns = @JoinColumn(name = "reserva_hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> huespedes;

    private LocalDate fechaEntrada;

    private LocalDate fechaSalida;

    private double montoTotal;
}