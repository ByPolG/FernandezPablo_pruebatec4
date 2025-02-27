package com.app.services;

import com.app.dtos.VueloDTO;
import java.util.List;

public interface VueloServiceInterfaz {

    List<VueloDTO> obtenerTodosLosVuelos();

    VueloDTO obtenerVueloPorCodigo(String codigo);

    VueloDTO guardarVuelo(VueloDTO vueloDTO);

    void eliminarVueloPorId(Long id); // Ya está implementado

    List<VueloDTO> buscarVuelosDisponibles(String dateFrom, String dateTo, String origin, String destination);

    VueloDTO editarVuelo(Long id, VueloDTO vueloDTO);
}
