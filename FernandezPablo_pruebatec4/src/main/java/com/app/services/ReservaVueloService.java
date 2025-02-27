package com.app.services;

import com.app.dtos.ReservaVueloDTO;
import com.app.entities.ReservaVuelo;
import com.app.entities.Usuario;
import com.app.entities.Vuelo;
import com.app.repositories.ReservaVueloRepositorio;
import com.app.repositories.VueloRepositorio;
import com.app.repositories.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaVueloService {

    @Autowired
    private ReservaVueloRepositorio reservaVueloRepository;

    @Autowired
    private VueloRepositorio vueloRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepository;

    public List<ReservaVueloDTO> obtenerTodasLasReservas() {
        return reservaVueloRepository.findAll().stream()
                .map(reserva -> new ReservaVueloDTO(
                        reserva.getId(),
                        reserva.getVuelo().getCodigoVuelo(),
                        reserva.getOrigen(),
                        reserva.getDestino(),
                        reserva.getFechaIda(),
                        reserva.getCantidadPersonas(),
                        reserva.getPasajeros().stream().map(Usuario::getId).collect(Collectors.toList()),  // Obtener solo los ID de los pasajeros
                        reserva.getMontoTotal()))
                .collect(Collectors.toList());
    }

    public ReservaVueloDTO obtenerReservaPorId(Long id) {
        ReservaVuelo reservaVuelo = reservaVueloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        return new ReservaVueloDTO(
                reservaVuelo.getId(),
                reservaVuelo.getVuelo().getCodigoVuelo(),
                reservaVuelo.getOrigen(),
                reservaVuelo.getDestino(),
                reservaVuelo.getFechaIda(),
                reservaVuelo.getCantidadPersonas(),
                reservaVuelo.getPasajeros().stream().map(Usuario::getId).collect(Collectors.toList()),  // Obtener solo los ID de los pasajeros
                reservaVuelo.getMontoTotal());
    }

    public ReservaVueloDTO guardarReserva(ReservaVueloDTO reservaVueloDTO) {
        // Buscamos el vuelo
        Vuelo vuelo = vueloRepositorio.findByOrigenAndDestinoAndFechaIda(
                reservaVueloDTO.getOrigen(),
                reservaVueloDTO.getDestino(),
                reservaVueloDTO.getFechaIda()
        ).orElseThrow(() -> new RuntimeException("No hay vuelos disponibles"));

        // Convertimos los IDs de pasajeros a objetos Usuario
        List<Usuario> pasajeros = reservaVueloDTO.getPasajeros().stream()
                .map(id -> usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado")))
                .collect(Collectors.toList());

        // Calculamos el monto total
        double montoTotal = vuelo.getPrecioVuelo() * reservaVueloDTO.getCantidadPersonas();

        // Creamos la reserva
        ReservaVuelo reservaVuelo = new ReservaVuelo(
                null,
                vuelo,
                reservaVueloDTO.getOrigen(),
                reservaVueloDTO.getDestino(),
                reservaVueloDTO.getFechaIda(),
                reservaVueloDTO.getCantidadPersonas(),
                pasajeros,
                montoTotal
        );

        reservaVueloRepository.save(reservaVuelo);

        return new ReservaVueloDTO(
                reservaVuelo.getId(),
                vuelo.getCodigoVuelo(),
                reservaVuelo.getOrigen(),
                reservaVuelo.getDestino(),
                reservaVuelo.getFechaIda(),
                reservaVuelo.getCantidadPersonas(),
                reservaVuelo.getPasajeros().stream().map(Usuario::getId).collect(Collectors.toList()),  // Solo enviar los ID de los pasajeros
                reservaVuelo.getMontoTotal()
        );
    }

    public void eliminarReserva(Long id) {
        reservaVueloRepository.deleteById(id);
    }
}
