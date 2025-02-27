package com.app.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VueloDTO {

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

    @JsonProperty("tipoAsiento")
    private String tipoAsiento;

    @JsonProperty("precioVuelo")
    private double precioVuelo;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("fechaIda")
    private LocalDate fechaIda;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("fechaVuelta")
    private LocalDate fechaVuelta;

    @JsonProperty("estado")
    private String estado;
}
