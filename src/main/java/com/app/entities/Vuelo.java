package com.app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Vuelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @JsonProperty("codigoVuelo")
    private String codigoVuelo;

    @JsonProperty("nombre")
    private String nombre;

    @JsonProperty("origen")
    private String origen;

    @JsonProperty("destino")
    private String destino;

    @Enumerated(EnumType.STRING)
    @JsonProperty("tipoAsiento")
    private TipoAsiento tipoAsiento;

    @JsonProperty("precioVuelo")
    private double precioVuelo;

    @JsonProperty("fechaIda")
    private LocalDate fechaIda;

    @JsonProperty("fechaVuelta")
    private LocalDate fechaVuelta;

    @Enumerated(EnumType.STRING)
    @JsonProperty("estado")
    private EstadoVuelo estado;

    public enum TipoAsiento {
        Economy,
        Business
    }

    public enum EstadoVuelo {
        DISPONIBLE,
        NO_DISPONIBLE
    }
}
