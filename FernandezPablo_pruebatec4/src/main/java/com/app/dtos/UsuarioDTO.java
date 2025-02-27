package com.app.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("nombre")
    private String nombre;

    @JsonProperty("contrasenia")
    private String contrasenia;

    @JsonProperty("rol")
    private String rol;

    @JsonProperty("reservasHotel")
    private List<ReservaHotelDTO> reservasHotel;

    @JsonProperty("reservasVuelo")
    private List<ReservaVueloDTO> reservasVuelo;
}
