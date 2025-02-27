package com.app.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigoHabitacion;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @Enumerated(EnumType.STRING)
    private TipoHabitacion tipo;

    @Enumerated(EnumType.STRING)
    @JsonProperty("estado")
    private EstadoHabitacion estado;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private Integer capacidad;

    @Column(name = "fecha_inicio")
    private LocalDate disponibleDesde;

    @Column(name = "fecha_fin")
    private LocalDate disponibleHasta;

    public enum TipoHabitacion {
        Single, Doble, Triple, Multiple
    }

    public enum EstadoHabitacion {
        DISPONIBLE,
        NO_DISPONIBLE
    }
}

