package com.app.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaHotelDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("hotelId")
    private Long hotelId;

    @JsonProperty("cantidadPersonas")
    private int cantidadPersonas;

    @JsonProperty("tipoHabitacion")
    private String tipoHabitacion;

    // Lista de IDs de los huéspedes (tipo Long)
    @JsonProperty("huespedes")
    private List<Long> huespedes;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("fechaEntrada")
    private LocalDate fechaEntrada;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("fechaSalida")
    private LocalDate fechaSalida;

    @JsonProperty("montoTotal")
    private double montoTotal;
}
