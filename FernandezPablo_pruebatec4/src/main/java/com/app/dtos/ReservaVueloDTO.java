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
public class ReservaVueloDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("codigoVuelo")
    private String codigoVuelo;

    @JsonProperty("origen")
    private String origen;

    @JsonProperty("destino")
    private String destino;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("fechaIda")
    private LocalDate fechaIda;

    @JsonProperty("cantidadPersonas")
    private int cantidadPersonas;

    @JsonProperty("pasajeros")
    private List<Long> pasajeros;

    @JsonProperty("montoTotal")
    private double montoTotal;
}
