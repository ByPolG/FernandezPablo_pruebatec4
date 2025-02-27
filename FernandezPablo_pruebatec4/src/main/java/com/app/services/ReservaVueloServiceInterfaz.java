package com.app.services;

import com.app.dtos.ReservaVueloDTO;

import java.util.List;

public interface ReservaVueloServiceInterfaz {

    List<ReservaVueloDTO> obtenerTodasLasReservas();

    ReservaVueloDTO obtenerReservaPorId(Long id);

    ReservaVueloDTO guardarReserva(ReservaVueloDTO reservaVueloDTO);

    void eliminarReserva(Long id);
}
