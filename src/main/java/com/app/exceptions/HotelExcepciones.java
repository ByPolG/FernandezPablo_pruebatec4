package com.app.exceptions;

public class HotelExcepciones {

    // Excepción para hotel no encontrado por ID

    public static class HotelNoEncontradoException extends RuntimeException {
        public HotelNoEncontradoException(Long id) {
            super("Hotel no encontrado con el ID: " + id);
        }
    }

    // Excepción para hotel no disponible (reservado)

    public static class HotelNoDisponibleException extends RuntimeException {
        public HotelNoDisponibleException(String mensaje) {
            super("Hotel no disponible: " + mensaje);
        }
    }
}
