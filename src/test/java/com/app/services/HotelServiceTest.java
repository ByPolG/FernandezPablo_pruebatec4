package com.app.services;

import com.app.dtos.HotelDTO;
import com.app.entities.Hotel;
import com.app.repositories.HotelRepositorio;
import com.app.exceptions.HotelExcepciones;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class HotelServiceTest {

    @Mock
    private HotelRepositorio hotelRepositorio;

    @InjectMocks
    private HotelService hotelService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarHoteles() {
        // Caso 1: Hay hoteles registrados
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setNombre("Hotel Test");
        hotel.setLugar("Ciudad");
        hotel.setPrecioPorNoche(100);
        when(hotelRepositorio.findAll()).thenReturn(List.of(hotel));

        List<HotelDTO> hoteles = hotelService.obtenerTodosLosHoteles();
        assertNotNull(hoteles);
        assertEquals(1, hoteles.size());
        assertEquals("Hotel Test", hoteles.get(0).getNombre());

        // Caso 2: No hay hoteles registrados
        when(hotelRepositorio.findAll()).thenReturn(Collections.emptyList());
        Exception exception = assertThrows(HotelExcepciones.HotelNoEncontradoException.class, () -> hotelService.obtenerTodosLosHoteles());
        assertEquals("Hotel no encontrado con el ID: -1", exception.getMessage());
    }

    @Test
    void testObtenerHotelPorId() {
        // Caso 1: El hotel existe
        Long hotelId = 1L;
        Hotel hotel = new Hotel();
        hotel.setId(hotelId);
        hotel.setNombre("Hotel Test");
        hotel.setLugar("Ciudad");
        hotel.setPrecioPorNoche(100);
        when(hotelRepositorio.findById(hotelId)).thenReturn(Optional.of(hotel));

        HotelDTO hotelDTO = hotelService.obtenerHotelPorId(hotelId);
        assertNotNull(hotelDTO);
        assertEquals(hotelId, hotelDTO.getId());
        assertEquals("Hotel Test", hotelDTO.getNombre());

        // Caso 2: El hotel no existe
        when(hotelRepositorio.findById(hotelId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(HotelExcepciones.HotelNoEncontradoException.class, () -> hotelService.obtenerHotelPorId(hotelId));
        assertEquals("Hotel no encontrado con el ID: " + hotelId, exception.getMessage());
    }
}
