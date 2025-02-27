package com.app.services;

import com.app.dtos.HabitacionDTO;
import com.app.dtos.ReservaHotelDTO;
import com.app.entities.Habitacion;
import com.app.entities.Hotel;
import com.app.entities.ReservaHotel;
import com.app.entities.Usuario;
import com.app.repositories.ReservaHotelRepositorio;
import com.app.repositories.HotelRepositorio;
import com.app.repositories.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaHotelService implements ReservaHotelServiceInterfaz {

    @Autowired
    private ReservaHotelRepositorio reservaHotelRepository;

    @Autowired
    private UsuarioRepositorio usuarioRepository;

    @Autowired
    private HotelRepositorio hotelRepositorio;

    @Autowired
    private HabitacionService habitacionServicio; // Agregado servicio de Habitacion

    public List<ReservaHotelDTO> obtenerTodasLasReservas() {
        return reservaHotelRepository.findAll().stream()
                .map(reserva -> {
                    // Obtener los IDs de los huéspedes
                    List<Long> huespedesIds = reserva.getHuespedes() != null ?
                            reserva.getHuespedes().stream().map(Usuario::getId).collect(Collectors.toList()) :
                            List.of();  // Si no hay huéspedes, devolver una lista vacía

                    // Crear y devolver el DTO con los datos adecuados
                    return new ReservaHotelDTO(
                            reserva.getId(),
                            reserva.getHotel().getId(),  // Usamos el ID del hotel
                            reserva.getCantidadPersonas(),
                            reserva.getTipoHabitacion(),
                            huespedesIds,  // Pasamos la lista de IDs de los huéspedes
                            reserva.getFechaEntrada(),
                            reserva.getFechaSalida(),
                            reserva.getMontoTotal()  // Si es necesario incluir montoTotal, lo pasamos también
                    );
                })
                .collect(Collectors.toList());
    }

    public ReservaHotelDTO obtenerReservaPorId(Long id) {
        ReservaHotel reservaHotel = reservaHotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        // Obtener los IDs de los huéspedes
        List<Long> huespedesIds = reservaHotel.getHuespedes() != null ?
                reservaHotel.getHuespedes().stream().map(Usuario::getId).collect(Collectors.toList()) :
                List.of();

        return new ReservaHotelDTO(
                reservaHotel.getId(),
                reservaHotel.getHotel().getId(),  // ID del hotel
                reservaHotel.getCantidadPersonas(),
                reservaHotel.getTipoHabitacion(),
                huespedesIds,
                reservaHotel.getFechaEntrada(),
                reservaHotel.getFechaSalida(),
                reservaHotel.getMontoTotal()
        );
    }

    public ReservaHotelDTO guardarReserva(ReservaHotelDTO reservaHotelDTO) {
        // Verificar si el hotel existe
        Hotel hotel = hotelRepositorio.findById(reservaHotelDTO.getHotelId())
                .orElseThrow(() -> new RuntimeException("Hotel no encontrado"));

        // Comprobar si el hotel tiene disponibilidad en las fechas solicitadas
        if (hotel.getDisponibleDesde().isAfter(reservaHotelDTO.getFechaSalida()) ||
                hotel.getDisponibleHasta().isBefore(reservaHotelDTO.getFechaEntrada())) {
            throw new RuntimeException("El hotel no está disponible en las fechas solicitadas");
        }

        // Buscar habitaciones disponibles en el hotel
        List<HabitacionDTO> habitacionesDisponibles = habitacionServicio.obtenerHabitacionesDisponibles(
                reservaHotelDTO.getFechaEntrada().toString(),
                reservaHotelDTO.getFechaSalida().toString(),
                hotel.getLugar()
        );

        // Filtrar habitaciones por el tipo solicitado (por ejemplo, "Doble")
        List<HabitacionDTO> habitacionesDelTipoSolicitado = habitacionesDisponibles.stream()
                .filter(h -> h.getTipoHabitacion().equals(reservaHotelDTO.getTipoHabitacion()))
                .collect(Collectors.toList());

        // Si no hay habitaciones disponibles del tipo solicitado, lanzamos una excepción
        if (habitacionesDelTipoSolicitado.isEmpty()) {
            throw new RuntimeException("No hay habitaciones disponibles del tipo solicitado");
        }

        // Seleccionar la primera habitación disponible del tipo solicitado
        HabitacionDTO habitacionDisponible = habitacionesDelTipoSolicitado.get(0);

        // Verificar los huéspedes
        List<Usuario> huespedes = reservaHotelDTO.getHuespedes() != null ?
                reservaHotelDTO.getHuespedes().stream()
                        .map(id -> usuarioRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Huésped no encontrado")))
                        .collect(Collectors.toList())
                : new ArrayList<>(); // Lista mutable de huéspedes

        // Calcular el monto total directamente en este método
        long noches = reservaHotelDTO.getFechaEntrada().until(reservaHotelDTO.getFechaSalida()).getDays();
        double montoTotal = noches * hotel.getPrecioPorNoche() * reservaHotelDTO.getCantidadPersonas();

        // Crear la reserva
        ReservaHotel reservaHotel = new ReservaHotel(
                null,
                hotel,
                reservaHotelDTO.getCantidadPersonas(),
                huespedes,  // Lista mutable de huéspedes
                reservaHotelDTO.getFechaEntrada(),
                reservaHotelDTO.getFechaSalida(),
                montoTotal,
                habitacionDisponible.getTipoHabitacion() // Aquí utilizamos el tipo de habitación correcto
        );

        // Guardar la reserva
        reservaHotel = reservaHotelRepository.save(reservaHotel);

        // Ahora, asignamos la lista de huéspedes (debe ser una lista mutable) y guardamos nuevamente
        reservaHotel.setHuespedes(huespedes);
        reservaHotel = reservaHotelRepository.save(reservaHotel);  // Guardar otra vez para actualizar la relación

        // Devolver el DTO con la reserva realizada
        return new ReservaHotelDTO(
                reservaHotel.getId(),
                hotel.getId(),
                reservaHotel.getCantidadPersonas(),
                habitacionDisponible.getTipoHabitacion(),
                reservaHotel.getHuespedes().stream()
                        .map(Usuario::getId)
                        .collect(Collectors.toList()), // Lista de IDs de los huéspedes
                reservaHotel.getFechaEntrada(),
                reservaHotel.getFechaSalida(),
                reservaHotel.getMontoTotal()
        );
    }


    @Override
    public void eliminarReserva(Long id) {
        reservaHotelRepository.deleteById(id);
    }
}
