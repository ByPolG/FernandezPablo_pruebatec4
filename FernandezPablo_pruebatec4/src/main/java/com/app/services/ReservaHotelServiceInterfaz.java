package com.app.services;

import com.app.dtos.ReservaHotelDTO;
import java.util.List;

public interface ReservaHotelServiceInterfaz {

    List<ReservaHotelDTO> obtenerTodasLasReservas();

    ReservaHotelDTO obtenerReservaPorId(Long id);

    ReservaHotelDTO guardarReserva(ReservaHotelDTO reservaHotelDTO);

    void eliminarReserva(Long id);
}
