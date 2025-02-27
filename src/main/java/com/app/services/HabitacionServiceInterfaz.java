package com.app.services;

import com.app.dtos.HabitacionDTO;
import java.util.List;

public interface HabitacionServiceInterfaz {

    List<HabitacionDTO> obtenerTodasLasHabitaciones();

    HabitacionDTO obtenerHabitacionPorId(Long id);

    HabitacionDTO guardarHabitacion(HabitacionDTO habitacionDTO);

    void eliminarHabitacion(Long id);

    List<HabitacionDTO> obtenerHabitacionesDisponibles(String dateFrom, String dateTo, String destination);
}
