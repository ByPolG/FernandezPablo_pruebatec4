package com.app.services;

import com.app.dtos.VueloDTO;
import com.app.entities.Vuelo;
import com.app.repositories.VueloRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VueloService implements VueloServiceInterfaz {

    @Autowired
    private VueloRepositorio vueloRepositorio;

    @Override
    public List<VueloDTO> obtenerTodosLosVuelos() {
        return vueloRepositorio.findAll().stream()
                .map(vuelo -> new VueloDTO(
                        vuelo.getId(),
                        vuelo.getCodigoVuelo(),
                        vuelo.getNombre(),
                        vuelo.getOrigen(),
                        vuelo.getDestino(),
                        vuelo.getTipoAsiento().name(),
                        vuelo.getPrecioVuelo(),
                        vuelo.getFechaIda(),
                        vuelo.getFechaVuelta(),
                        vuelo.getEstado().name()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public VueloDTO obtenerVueloPorCodigo(String codigo) {
        Vuelo vuelo = vueloRepositorio.findByCodigoVuelo(codigo);

        if (vuelo == null) {
            throw new RuntimeException("Vuelo no encontrado con el código: " + codigo);
        }

        return new VueloDTO(
                vuelo.getId(),
                vuelo.getCodigoVuelo(),
                vuelo.getNombre(),
                vuelo.getOrigen(),
                vuelo.getDestino(),
                vuelo.getTipoAsiento().name(),
                vuelo.getPrecioVuelo(),
                vuelo.getFechaIda(),
                vuelo.getFechaVuelta(),
                vuelo.getEstado().name()
        );
    }

    @Override
    public List<VueloDTO> buscarVuelosDisponibles(String dateFrom, String dateTo, String origin, String destination) {
        LocalDate from = LocalDate.parse(dateFrom);
        LocalDate to = LocalDate.parse(dateTo);

        return vueloRepositorio.findAll().stream()
                .filter(vuelo -> !vuelo.getFechaIda().isBefore(from) && !vuelo.getFechaVuelta().isAfter(to))
                .filter(vuelo -> vuelo.getOrigen().trim().equalsIgnoreCase(origin.trim()))
                .filter(vuelo -> vuelo.getDestino().trim().equalsIgnoreCase(destination.trim()))
                .filter(vuelo -> vuelo.getEstado() == Vuelo.EstadoVuelo.DISPONIBLE) // ✅ Filtramos solo vuelos disponibles
                .map(vuelo -> new VueloDTO(
                        vuelo.getId(),
                        vuelo.getCodigoVuelo(),
                        vuelo.getNombre(),
                        vuelo.getOrigen(),
                        vuelo.getDestino(),
                        vuelo.getTipoAsiento().name(),
                        vuelo.getPrecioVuelo(),
                        vuelo.getFechaIda(),
                        vuelo.getFechaVuelta(),
                        vuelo.getEstado().name()
                ))
                .collect(Collectors.toList());
    }

    private String generarCodigoVuelo() {
        // Obtener el último vuelo con el código más alto
        Vuelo ultimoVuelo = vueloRepositorio.findTopByOrderByCodigoVueloDesc();

        // Si no hay vuelos, comenzamos con "VL001"
        if (ultimoVuelo == null) {
            return "VL001";
        }

        // Extraemos el código de vuelo
        String ultimoCodigo = ultimoVuelo.getCodigoVuelo();

        // Extraemos la parte numérica del código (después de 'VL')
        String numeroStr = ultimoCodigo.substring(2); // 'VL' tiene longitud 2
        int numero = Integer.parseInt(numeroStr);

        // Incrementamos el número
        numero++;

        // Generamos el nuevo código con el formato 'VL###' donde ### es un número de tres dígitos
        return String.format("VL%03d", numero);
    }

    @Override
    public VueloDTO guardarVuelo(VueloDTO vueloDTO) {
        // Generamos el código de vuelo automáticamente
        String codigoVuelo = generarCodigoVuelo();

        // Crear un objeto Vuelo a partir del VueloDTO, usando el código generado
        Vuelo vuelo = new Vuelo(
                null,  // Dejamos 'id' como null, ya que lo genera la base de datos
                codigoVuelo,  // Código generado automáticamente
                vueloDTO.getNombre(),
                vueloDTO.getOrigen(),
                vueloDTO.getDestino(),
                Vuelo.TipoAsiento.valueOf(vueloDTO.getTipoAsiento()),
                vueloDTO.getPrecioVuelo(),
                vueloDTO.getFechaIda(),
                vueloDTO.getFechaVuelta(),
                Vuelo.EstadoVuelo.valueOf(vueloDTO.getEstado())
        );

        // Guardar el vuelo en la base de datos (la base de datos asignará el 'id')
        vuelo = vueloRepositorio.save(vuelo);

        // Devolver el VueloDTO con el 'id' y 'codigoVuelo' generado por la base de datos
        return new VueloDTO(
                vuelo.getId(),
                vuelo.getCodigoVuelo(),
                vuelo.getNombre(),
                vuelo.getOrigen(),
                vuelo.getDestino(),
                vuelo.getTipoAsiento().name(),
                vuelo.getPrecioVuelo(),
                vuelo.getFechaIda(),
                vuelo.getFechaVuelta(),
                vuelo.getEstado().name()
        );
    }

    @Override
    public void eliminarVueloPorId(Long id) {
        // Verificar si el vuelo existe
        Vuelo vuelo = vueloRepositorio.findById(id).orElseThrow(() ->
                new RuntimeException("Vuelo no encontrado con el ID: " + id));

        // Verificar si el vuelo está disponible o no
        if (vuelo.getEstado() == Vuelo.EstadoVuelo.NO_DISPONIBLE) {
            throw new RuntimeException("No se puede eliminar el vuelo. El vuelo está reservado.");
        }

        // Eliminar el vuelo si está disponible
        vueloRepositorio.deleteById(id);
    }


    @Override
    public VueloDTO editarVuelo(Long id, VueloDTO vueloDTO) {
        // Buscar el vuelo existente por su ID
        Vuelo vueloExistente = vueloRepositorio.findById(id).orElseThrow(() ->
                new RuntimeException("Vuelo no encontrado con el ID: " + id));

        // Verificar el estado del vuelo. Si está NO_DISPONIBLE, no se puede editar.
        if (vueloExistente.getEstado() == Vuelo.EstadoVuelo.NO_DISPONIBLE) {
            throw new RuntimeException("No se puede editar el vuelo. El vuelo ya está reservado.");
        }

        // Si el vuelo está disponible, podemos proceder a editarlo
        vueloExistente.setCodigoVuelo(vueloDTO.getCodigoVuelo());  // No se debe cambiar el id, solo los demás campos
        vueloExistente.setNombre(vueloDTO.getNombre());
        vueloExistente.setOrigen(vueloDTO.getOrigen());
        vueloExistente.setDestino(vueloDTO.getDestino());
        vueloExistente.setTipoAsiento(Vuelo.TipoAsiento.valueOf(vueloDTO.getTipoAsiento()));
        vueloExistente.setPrecioVuelo(vueloDTO.getPrecioVuelo());
        vueloExistente.setFechaIda(vueloDTO.getFechaIda());
        vueloExistente.setFechaVuelta(vueloDTO.getFechaVuelta());
        vueloExistente.setEstado(Vuelo.EstadoVuelo.valueOf(vueloDTO.getEstado()));  // El estado también puede ser editado si lo deseas

        // Guardar el vuelo actualizado
        vueloRepositorio.save(vueloExistente);

        // Retornar el DTO actualizado
        return new VueloDTO(
                vueloExistente.getId(),
                vueloExistente.getCodigoVuelo(),
                vueloExistente.getNombre(),
                vueloExistente.getOrigen(),
                vueloExistente.getDestino(),
                vueloExistente.getTipoAsiento().name(),
                vueloExistente.getPrecioVuelo(),
                vueloExistente.getFechaIda(),
                vueloExistente.getFechaVuelta(),
                vueloExistente.getEstado().name()
        );
    }

}