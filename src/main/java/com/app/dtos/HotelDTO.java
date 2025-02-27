package com.app.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelDTO {

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

    @JsonProperty("estado")
    private String estado;
}
