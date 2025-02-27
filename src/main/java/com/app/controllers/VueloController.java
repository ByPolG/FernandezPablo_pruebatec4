package com.app.controllers;

import com.app.dtos.VueloDTO;
import com.app.dtos.ReservaVueloDTO;
import com.app.exceptions.VueloExcepciones;
import com.app.services.VueloService;
import com.app.services.ReservaVueloService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agency")
public class VueloController {

    @Autowired
    private VueloService vueloService;

    @Autowired
    private ReservaVueloService reservaVueloService;

    @Operation(summary = "Para obtener todos los vuelos", description = "Este endpoint devuelve una lista de todos los vuelos disponibles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de vuelos obtenida correctamente.")
    })
    @GetMapping("/flights")
    public ResponseEntity<List<VueloDTO>> obtenerTodosLosVuelos() {
        List<VueloDTO> vuelos = vueloService.obtenerTodosLosVuelos();
        return ResponseEntity.status(200).body(vuelos);
    }

    @Operation(summary = "Para buscar vuelos disponibles por fecha y destino", description = "Este endpoint devuelve los vuelos disponibles en un rango de fechas y destinos específicos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de vuelos encontrados."),
            @ApiResponse(responseCode = "400", description = "Datos de búsqueda incorrectos.")
    })
    @GetMapping("/flights/search")
    public ResponseEntity<List<VueloDTO>> buscarVuelos(
            @Parameter(description = "Fecha de inicio de la búsqueda de vuelos") @RequestParam String dateFrom,
            @Parameter(description = "Fecha de fin de la búsqueda de vuelos") @RequestParam String dateTo,
            @Parameter(description = "Origen de los vuelos") @RequestParam String origin,
            @Parameter(description = "Destino de los vuelos") @RequestParam String destination) {
        List<VueloDTO> vuelosDisponibles = vueloService.buscarVuelosDisponibles(dateFrom, dateTo, origin, destination);
        return ResponseEntity.status(200).body(vuelosDisponibles);
    }

    @Operation(summary = "Para reservar un vuelo", description = "Este endpoint permite realizar una reserva de un vuelo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva realizada con éxito."),
            @ApiResponse(responseCode = "400", description = "Datos de la reserva incorrectos.")
    })
    @PostMapping("/flight-booking/new")
    public ResponseEntity<ReservaVueloDTO> reservarVuelo(@RequestBody ReservaVueloDTO reservaVueloDTO) {
        ReservaVueloDTO reservaGuardada = reservaVueloService.guardarReserva(reservaVueloDTO);
        return ResponseEntity.status(201).body(reservaGuardada);
    }

    @Operation(summary = "Para crear un nuevo vuelo", description = "Este endpoint permite registrar un nuevo vuelo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vuelo creado con éxito."),
            @ApiResponse(responseCode = "400", description = "Datos de vuelo incorrectos.")
    })
    @PostMapping("/flights/new")
    public ResponseEntity<VueloDTO> crearVuelo(@RequestBody VueloDTO vueloDTO) {
        VueloDTO vueloCreado = vueloService.guardarVuelo(vueloDTO);
        return ResponseEntity.status(201).body(vueloCreado);
    }

    @Operation(summary = "Para editar un vuelo por su ID", description = "Este endpoint permite editar un vuelo existente. Solo se podrá editar si el vuelo no está reservado (su estado es DISPONIBLE).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vuelo editado con éxito."),
            @ApiResponse(responseCode = "400", description = "No se puede editar el vuelo, ya que está reservado o no se encuentra disponible."),
            @ApiResponse(responseCode = "404", description = "Vuelo no encontrado con el ID proporcionado.")
    })
    @PutMapping("/flights/edit/{id}")
    public ResponseEntity<VueloDTO> editarVuelo(
            @Parameter(description = "ID del vuelo que se desea editar") @PathVariable Long id,
            @Parameter(description = "Datos del vuelo con la información actualizada") @RequestBody VueloDTO vueloDTO) {

        VueloDTO vueloEditado = vueloService.editarVuelo(id, vueloDTO);
        return ResponseEntity.status(200).body(vueloEditado);
    }

    @Operation(summary = "Para eliminar un vuelo por su ID",
            description = "Este endpoint permite eliminar un vuelo existente por su ID. Solo se podrá eliminar si el vuelo está DISPONIBLE.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vuelo eliminado con éxito."),
            @ApiResponse(responseCode = "400", description = "No se puede eliminar el vuelo, ya que está reservado o no se encuentra disponible."),
            @ApiResponse(responseCode = "404", description = "Vuelo no encontrado con el ID proporcionado.")
    })
    @DeleteMapping("/flights/delete/{id}")
    public ResponseEntity<String> eliminarVuelo(
            @Parameter(description = "ID del vuelo que se desea eliminar") @PathVariable Long id) {
        try {
            vueloService.eliminarVueloPorId(id);
            return ResponseEntity.status(200).body("Vuelo eliminado con éxito.");
        } catch (VueloExcepciones.VueloNoEncontradoException e) {
            return ResponseEntity.status(404).body("Vuelo no encontrado con el ID proporcionado.");
        } catch (VueloExcepciones.VueloNoDisponibleException e) {
            return ResponseEntity.status(400).body("No se puede eliminar el vuelo, ya que está reservado o no se encuentra disponible.");
        }
    }



}
