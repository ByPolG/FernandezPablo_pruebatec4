package com.app.exceptions;

public class VueloExcepciones {

    // Excepción para vuelo no encontrado por ID

    public static class VueloNoEncontradoException extends RuntimeException {
        public VueloNoEncontradoException(Long id) {
            super("Vuelo no encontrado con el ID: " + id);
        }
    }

    // Excepción para vuelo no disponible

    public static class VueloNoDisponibleException extends RuntimeException {
        public VueloNoDisponibleException(String mensaje) {
            super("Vuelo no disponible: " + mensaje);
        }
    }
}
