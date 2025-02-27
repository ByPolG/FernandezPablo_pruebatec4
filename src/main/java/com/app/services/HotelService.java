package com.app.services;

import com.app.dtos.HotelDTO;
import com.app.dtos.VueloDTO;
import com.app.entities.Hotel;
import com.app.entities.Vuelo;
import com.app.repositories.HotelRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                        hotel.isReservado() ? "NO_DISPONIBLE" : "DISPONIBLE" // Usamos el campo reservado para definir el estado
                ))
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
                hotel.isReservado() ? "NO_DISPONIBLE" : "DISPONIBLE" // Usamos el campo reservado para definir el estado
        );
    }

    private String generarCodigoHotel() {
        // Obtener el último hotel con el código más alto
        Hotel ultimoHotel = hotelRepositorio.findTopByOrderByCodigoHotelDesc();

        // Si no hay hoteles, comenzamos con "HOT001"
        if (ultimoHotel == null) {
            return "HOT001";
        }

        // Extraemos el código de hotel
        String ultimoCodigo = ultimoHotel.getCodigoHotel();

        // Extraemos la parte numérica del código (después de 'HOT')
        String numeroStr = ultimoCodigo.substring(3); // 'HOT' tiene longitud 3
        int numero = Integer.parseInt(numeroStr);

        // Incrementamos el número
        numero++;

        // Generamos el nuevo código con el formato 'HOT###' donde ### es un número de tres dígitos
        return String.format("HOT%03d", numero);
    }


    @Override
    public HotelDTO guardarHotel(HotelDTO hotelDTO) {
        // Generamos el código del hotel automáticamente
        String codigoHotel = generarCodigoHotel();

        // Creamos un objeto Hotel sin utilizar el constructor con parámetros
        Hotel hotel = new Hotel();  // Usamos el constructor sin parámetros

        // Asignamos los valores utilizando los métodos set
        hotel.setCodigoHotel(codigoHotel);  // Usamos el código generado automáticamente
        hotel.setNombre(hotelDTO.getNombre());
        hotel.setLugar(hotelDTO.getLugar());
        hotel.setPrecioPorNoche(hotelDTO.getPrecioPorNoche());
        hotel.setDisponibleDesde(hotelDTO.getDisponibleDesde());
        hotel.setDisponibleHasta(hotelDTO.getDisponibleHasta());
        hotel.setReservado(hotelDTO.getEstado().equals("DISPONIBLE") ? false : true); // Usamos el campo reservado

        // Guardamos el hotel en la base de datos
        hotel = hotelRepositorio.save(hotel);

        // Retornamos el DTO con los datos del hotel recién guardado
        return new HotelDTO(
                hotel.getId(),
                hotel.getCodigoHotel(),
                hotel.getNombre(),
                hotel.getLugar(),
                hotel.getPrecioPorNoche(),
                hotel.getDisponibleDesde(),
                hotel.getDisponibleHasta(),
                hotelDTO.getEstado() // Retornamos el estado proporcionado
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
        hotel.setReservado(hotelDTO.getEstado().equals("DISPONIBLE") ? false : true);  // Usamos el campo reservado

        hotel = hotelRepositorio.save(hotel);

        return new HotelDTO(
                hotel.getId(),
                hotel.getCodigoHotel(),
                hotel.getNombre(),
                hotel.getLugar(),
                hotel.getPrecioPorNoche(),
                hotel.getDisponibleDesde(),
                hotel.getDisponibleHasta(),
                hotelDTO.getEstado() // Retornamos el estado proporcionado
        );
    }
}
