package com.app.controllers;

import com.app.dtos.HotelDTO;
import com.app.dtos.HabitacionDTO;
import com.app.dtos.ReservaHotelDTO;
import com.app.services.HotelService;
import com.app.services.HabitacionService;
import com.app.services.ReservaHotelService;
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
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private HabitacionService habitacionService;

    @Autowired
    private ReservaHotelService reservaHotelService;

    @Operation(summary = "Para obtener todos los hoteles", description = "Este endpoint devuelve una lista de todos los hoteles disponibles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de hoteles obtenida correctamente.")
    })
    @GetMapping("/hotels")
    public ResponseEntity<List<HotelDTO>> obtenerTodosLosHoteles() {
        List<HotelDTO> hoteles = hotelService.obtenerTodosLosHoteles();
        return ResponseEntity.status(200).body(hoteles);
    }

    @Operation(summary = "Para obtener habitaciones disponibles en un rango de fechas y destino", description = "Este endpoint devuelve habitaciones disponibles en un rango de fechas y destino específicos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de habitaciones disponibles obtenida correctamente.")
    })
    @GetMapping("/rooms")
    public ResponseEntity<List<HabitacionDTO>> obtenerHabitacionesDisponibles(
            @Parameter(description = "Fecha de inicio de la reserva") @RequestParam String dateFrom,
            @Parameter(description = "Fecha de fin de la reserva") @RequestParam String dateTo,
            @Parameter(description = "Destino del hotel") @RequestParam String destination) {
        List<HabitacionDTO> habitacionesDisponibles = habitacionService.obtenerHabitacionesDisponibles(dateFrom, dateTo, destination);
        return ResponseEntity.status(200).body(habitacionesDisponibles);
    }

    @Operation(summary = "Para crear nuevo hotel", description = "Este endpoint permite crear un nuevo hotel.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Hotel creado correctamente."),
            @ApiResponse(responseCode = "400", description = "Datos de hotel incorrectos.")
    })
    @PostMapping("/hotels/new")
    public ResponseEntity<HotelDTO> crearHotel(@RequestBody HotelDTO hotelDTO) {
        HotelDTO hotelCreado = hotelService.guardarHotel(hotelDTO);
        return ResponseEntity.status(201).body(hotelCreado);
    }

    @Operation(summary = "Para obtener un hotel por su ID", description = "Este endpoint devuelve la información de un hotel según su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotel encontrado."),
            @ApiResponse(responseCode = "404", description = "Hotel no encontrado.")
    })
    @GetMapping("/hotels/{id}")
    public ResponseEntity<HotelDTO> obtenerHotelPorId(@PathVariable Long id) {
        HotelDTO hotel = hotelService.obtenerHotelPorId(id);
        return hotel != null ? ResponseEntity.status(200).body(hotel)
                : ResponseEntity.status(404).build();
    }
}
