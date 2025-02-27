package com.app.services;

import com.app.dtos.HotelDTO;
import java.util.List;

public interface HotelServiceInterfaz {

    List<HotelDTO> obtenerTodosLosHoteles();

    HotelDTO obtenerHotelPorId(Long id);

    HotelDTO guardarHotel(HotelDTO hotelDTO);

    void eliminarHotel(Long id);

    void eliminarHotelPorCodigo(String codigo);

    HotelDTO editarHotel(Long id, HotelDTO hotelDTO);
}
