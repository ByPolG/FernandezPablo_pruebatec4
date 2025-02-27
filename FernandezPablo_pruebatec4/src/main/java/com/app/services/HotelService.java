package com.app.services;

import com.app.dtos.HotelDTO;
import com.app.entities.Hotel;
import com.app.repositories.HotelRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelService implements HotelServiceInterfaz {

    @Autowired
    private HotelRepositorio hotelRepositorio;

    @Override
    public List<HotelDTO> obtenerTodosLosHoteles() {
        return hotelRepositorio.findAll().stream()
                .map(hotel -> new HotelDTO(
                        hotel.getId(),
                        hotel.getCodigoHotel(),
                        hotel.getNombre(),
                        hotel.getLugar(),
                        hotel.getPrecioPorNoche(),
                        hotel.getDisponibleDesde(),
                        hotel.getDisponibleHasta(),
                        hotel.isReservado()))
                .collect(Collectors.toList());
    }

    @Override
    public HotelDTO obtenerHotelPorId(Long id) {
        Hotel hotel = hotelRepositorio.findById(id).orElse(null);
        if (hotel == null) {
            return null;
        }
        return new HotelDTO(
                hotel.getId(),
                hotel.getCodigoHotel(),
                hotel.getNombre(),
                hotel.getLugar(),
                hotel.getPrecioPorNoche(),
                hotel.getDisponibleDesde(),
                hotel.getDisponibleHasta(),
                hotel.isReservado());
    }

    @Override
    public HotelDTO guardarHotel(HotelDTO hotelDTO) {
        // Creamos un objeto Hotel, pasando una lista vacía de habitaciones
        Hotel hotel = new Hotel(
                null,  // ID generado automáticamente
                hotelDTO.getCodigoHotel(),
                hotelDTO.getNombre(),
                hotelDTO.getLugar(),
                hotelDTO.getPrecioPorNoche(),
                hotelDTO.getDisponibleDesde(),
                hotelDTO.getDisponibleHasta(),
                hotelDTO.isReservado(),
                new ArrayList<>()  // Lista vacía de habitaciones
        );
        hotel = hotelRepositorio.save(hotel);

        return new HotelDTO(
                hotel.getId(),
                hotel.getCodigoHotel(),
                hotel.getNombre(),
                hotel.getLugar(),
                hotel.getPrecioPorNoche(),
                hotel.getDisponibleDesde(),
                hotel.getDisponibleHasta(),
                hotel.isReservado()
        );
    }

    @Override
    public void eliminarHotelPorCodigo(String codigoHotel) {
        // Buscamos el hotel por su código
        Hotel hotel = hotelRepositorio.findByCodigoHotel(codigoHotel);

        // Si el hotel no existe, lanzamos una excepción
        if (hotel == null) {
            throw new RuntimeException("Hotel no encontrado con el código: " + codigoHotel);
        }

        // Eliminamos el hotel encontrado
        hotelRepositorio.deleteById(hotel.getId());
    }


    @Override
    public void eliminarHotel(Long id) {
        if (!hotelRepositorio.existsById(id)) {
            throw new RuntimeException("Hotel no encontrado con el ID: " + id);
        }
        hotelRepositorio.deleteById(id);
    }

    @Override
    public HotelDTO editarHotel(Long id, HotelDTO hotelDTO) {
        Hotel hotel = hotelRepositorio.findById(id).orElse(null);
        if (hotel == null) {
            throw new RuntimeException("Hotel no encontrado con el ID: " + id);
        }

        hotel.setCodigoHotel(hotelDTO.getCodigoHotel());
        hotel.setNombre(hotelDTO.getNombre());
        hotel.setLugar(hotelDTO.getLugar());
        hotel.setPrecioPorNoche(hotelDTO.getPrecioPorNoche());
        hotel.setDisponibleDesde(hotelDTO.getDisponibleDesde());
        hotel.setDisponibleHasta(hotelDTO.getDisponibleHasta());
        hotel.setReservado(hotelDTO.isReservado());

        hotel = hotelRepositorio.save(hotel);

        return new HotelDTO(
                hotel.getId(),
                hotel.getCodigoHotel(),
                hotel.getNombre(),
                hotel.getLugar(),
                hotel.getPrecioPorNoche(),
                hotel.getDisponibleDesde(),
                hotel.getDisponibleHasta(),
                hotel.isReservado()
        );
    }
}