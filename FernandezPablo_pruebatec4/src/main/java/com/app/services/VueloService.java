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
    private VueloRepositorio vueloRepository;

    @Override
    public List<VueloDTO> obtenerTodosLosVuelos() {
        return vueloRepository.findAll().stream()
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
        Vuelo vuelo = vueloRepository.findByCodigoVuelo(codigo);

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

        return vueloRepository.findAll().stream()
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

    @Override
    public VueloDTO guardarVuelo(VueloDTO vueloDTO) {
        Vuelo vuelo = new Vuelo(
                null,  // Dejamos 'id' como null, ya que lo genera la base de datos
                vueloDTO.getCodigoVuelo(),
                vueloDTO.getNombre(),
                vueloDTO.getOrigen(),
                vueloDTO.getDestino(),
                Vuelo.TipoAsiento.valueOf(vueloDTO.getTipoAsiento()),
                vueloDTO.getPrecioVuelo(),
                vueloDTO.getFechaIda(),
                vueloDTO.getFechaVuelta(),
                Vuelo.EstadoVuelo.valueOf(vueloDTO.getEstado())
        );
        vueloRepository.save(vuelo);
        return vueloDTO;
    }

    @Override
    public void eliminarVueloPorId(Long id) {
        if (!vueloRepository.existsById(id)) {
            throw new RuntimeException("Vuelo no encontrado con el ID: " + id);
        }
        vueloRepository.deleteById(id);
    }

    @Override
    public VueloDTO editarVuelo(Long id, VueloDTO vueloDTO) {
        Vuelo vueloExistente = vueloRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Vuelo no encontrado con el ID: " + id));

        vueloExistente.setCodigoVuelo(vueloDTO.getCodigoVuelo());
        vueloExistente.setNombre(vueloDTO.getNombre());
        vueloExistente.setOrigen(vueloDTO.getOrigen());
        vueloExistente.setDestino(vueloDTO.getDestino());
        vueloExistente.setTipoAsiento(Vuelo.TipoAsiento.valueOf(vueloDTO.getTipoAsiento()));
        vueloExistente.setPrecioVuelo(vueloDTO.getPrecioVuelo());
        vueloExistente.setFechaIda(vueloDTO.getFechaIda());
        vueloExistente.setFechaVuelta(vueloDTO.getFechaVuelta());
        vueloExistente.setEstado(Vuelo.EstadoVuelo.valueOf(vueloDTO.getEstado()));

        vueloRepository.save(vueloExistente);

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