package com.app.services;

import com.app.dtos.ReservaHotelDTO;
import com.app.entities.ReservaHotel;
import com.app.entities.Usuario;
import com.app.repositories.ReservaHotelRepositorio;
import com.app.repositories.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaHotelService implements ReservaHotelServiceInterfaz {

    @Autowired
    private ReservaHotelRepositorio reservaHotelRepository;

    @Autowired
    private UsuarioRepositorio usuarioRepository;

    public List<ReservaHotelDTO> obtenerTodasLasReservas() {
        return reservaHotelRepository.findAll().stream()
                .map(reserva -> new ReservaHotelDTO(
                        reserva.getId(),
                        reserva.getHotel().getCodigoHotel(),
                        reserva.getCantidadPersonas(),
                        reserva.getHuespedes().stream().map(Usuario::getId).collect(Collectors.toList()),  // Obtener solo los ID de los huéspedes
                        reserva.getFechaEntrada(),
                        reserva.getFechaSalida(),
                        reserva.getMontoTotal()))
                .collect(Collectors.toList());
    }

    public ReservaHotelDTO obtenerReservaPorId(Long id) {
        ReservaHotel reservaHotel = reservaHotelRepository.findById(id).orElseThrow();
        return new ReservaHotelDTO(
                reservaHotel.getId(),
                reservaHotel.getHotel().getCodigoHotel(),
                reservaHotel.getCantidadPersonas(),
                reservaHotel.getHuespedes().stream().map(Usuario::getId).collect(Collectors.toList()),  // Obtener solo los ID de los huéspedes
                reservaHotel.getFechaEntrada(),
                reservaHotel.getFechaSalida(),
                reservaHotel.getMontoTotal());
    }

    public ReservaHotelDTO guardarReserva(ReservaHotelDTO reservaHotelDTO) {
        // Convertimos los IDs de huéspedes a objetos Usuario
        List<Usuario> huespedes = reservaHotelDTO.getHuespedes().stream()
                .map(id -> usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado")))
                .collect(Collectors.toList());

        // Creamos y guardar la reserva
        ReservaHotel reservaHotel = new ReservaHotel(
                null,
                null, // El hotel debe ser obtenido por código
                reservaHotelDTO.getCantidadPersonas(),
                huespedes,
                reservaHotelDTO.getFechaEntrada(),
                reservaHotelDTO.getFechaSalida(),
                reservaHotelDTO.getMontoTotal()
        );

        reservaHotelRepository.save(reservaHotel);

        return new ReservaHotelDTO(
                reservaHotel.getId(),
                reservaHotel.getHotel().getCodigoHotel(),
                reservaHotel.getCantidadPersonas(),
                huespedes.stream().map(Usuario::getId).collect(Collectors.toList()),  // Solo enviar los IDs de los huéspedes
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
