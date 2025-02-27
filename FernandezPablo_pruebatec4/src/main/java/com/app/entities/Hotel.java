package com.app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;  // Asegúrate de importar la clase List

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @JsonProperty("codigoHotel")
    private String codigoHotel;

    @JsonProperty("nombre")
    private String nombre;

    @JsonProperty("lugar")
    private String lugar;

    @JsonProperty("precioPorNoche")
    private double precioPorNoche;

    @JsonProperty("disponibleDesde")
    private LocalDate disponibleDesde;

    @JsonProperty("disponibleHasta")
    private LocalDate disponibleHasta;

    @JsonProperty("reservado")
    private boolean reservado;

    @OneToMany(mappedBy = "hotel")
    private List<Habitacion> habitaciones;
}
