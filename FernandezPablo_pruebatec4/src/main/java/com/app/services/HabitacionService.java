package com.app.services;

import com.app.dtos.HabitacionDTO;
import com.app.entities.Habitacion;
import com.app.entities.Hotel;
import com.app.repositories.HabitacionRepositorio;
import com.app.repositories.HotelRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HabitacionService implements HabitacionServiceInterfaz {

    @Autowired
    private HabitacionRepositorio habitacionRepositorio;

    @Autowired
    private HotelRepositorio hotelRepositorio;

    @Override
    public List<HabitacionDTO> obtenerTodasLasHabitaciones() {
        return habitacionRepositorio.findAll().stream()
                .map(habitacion -> new HabitacionDTO(
                        habitacion.getId(),
                        habitacion.getCodigoHabitacion(),
                        habitacion.getTipo().name(),
                        habitacion.getPrecio(),
                        habitacion.getCapacidad(),
                        habitacion.getEstado().name(),  // Usamos el nombre del estado (enum)
                        habitacion.getHotel().getCodigoHotel(),
                        habitacion.getDisponibleDesde(),
                        habitacion.getDisponibleHasta()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public HabitacionDTO obtenerHabitacionPorId(Long id) {
        Habitacion habitacion = habitacionRepositorio.findById(id).orElseThrow();
        return new HabitacionDTO(
                habitacion.getId(),
                habitacion.getCodigoHabitacion(),
                habitacion.getTipo().name(),
                habitacion.getPrecio(),
                habitacion.getCapacidad(),
                habitacion.getEstado().name(),  // Usamos el nombre del estado (enum)
                habitacion.getHotel().getCodigoHotel(),
                habitacion.getDisponibleDesde(),
                habitacion.getDisponibleHasta()
        );
    }

    @Override
    public HabitacionDTO guardarHabitacion(HabitacionDTO habitacionDTO) {
        // Buscar el hotel por código
        Hotel hotel = hotelRepositorio.findByCodigoHotel(habitacionDTO.getCodigoHotel());
        if (hotel == null) {
            throw new RuntimeException("Hotel no encontrado con el código: " + habitacionDTO.getCodigoHotel());
        }

        // Convertimos el String del DTO a Enum EstadoHabitacion
        Habitacion.EstadoHabitacion estadoHabitacion = Habitacion.EstadoHabitacion.valueOf(habitacionDTO.getEstado());

        // Creamos la habitación con el hotel correspondiente
        Habitacion habitacion = new Habitacion(
                null,  // ID generado automáticamente
                habitacionDTO.getCodigoHabitacion(),
                hotel,  // Asignar el hotel
                Habitacion.TipoHabitacion.valueOf(habitacionDTO.getTipoHabitacion()),
                estadoHabitacion,
                habitacionDTO.getPrecioPorNoche(),
                habitacionDTO.getCapacidad(),
                habitacionDTO.getDisponibleDesde(),
                habitacionDTO.getDisponibleHasta()
        );

        // Guardamos la habitación
        habitacionRepositorio.save(habitacion);

        return habitacionDTO;
    }


    @Override
    public void eliminarHabitacion(Long id) {
        habitacionRepositorio.deleteById(id);
    }

    @Override
    public List<HabitacionDTO> obtenerHabitacionesDisponibles(String dateFrom, String dateTo, String destination) {
        LocalDate fechaInicio = LocalDate.parse(dateFrom);
        LocalDate fechaFin = LocalDate.parse(dateTo);

        return habitacionRepositorio.findAll().stream()
                .filter(habitacion -> habitacion.getHotel().getLugar().equalsIgnoreCase(destination) &&
                        habitacion.getDisponibleDesde().compareTo(fechaInicio) <= 0 &&
                        habitacion.getDisponibleHasta().compareTo(fechaFin) >= 0 &&
                        habitacion.getEstado() == Habitacion.EstadoHabitacion.DISPONIBLE) // Filtrar por estado DISPONIBLE
                .map(habitacion -> new HabitacionDTO(
                        habitacion.getId(),
                        habitacion.getCodigoHabitacion(),
                        habitacion.getTipo().name(),
                        habitacion.getPrecio(),
                        habitacion.getCapacidad(),
                        habitacion.getEstado().name(),  // Usamos el nombre del estado (enum)
                        habitacion.getHotel().getCodigoHotel(),
                        habitacion.getDisponibleDesde(),
                        habitacion.getDisponibleHasta()
                ))
                .collect(Collectors.toList());
    }
}
