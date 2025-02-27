package com.app.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitacionDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("codigoHabitacion")
    private String codigoHabitacion;

    @JsonProperty("tipoHabitacion")
    private String tipoHabitacion;

    @JsonProperty("precioHabitacion")
    private double precioHabitacion;

    @JsonProperty("capacidad")
    private int capacidad;

    @JsonProperty("estado")
    private String estado;

    @JsonProperty("codigoHotel")
    private String codigoHotel;

    @JsonProperty("fechaInicio")
    private LocalDate disponibleDesde;

    @JsonProperty("fechaFin")
    private LocalDate disponibleHasta;
}
