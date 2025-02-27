package com.app.controllers;

import com.app.dtos.HotelDTO;
import com.app.dtos.HabitacionDTO;
import com.app.dtos.ReservaHotelDTO;
import com.app.exceptions.HotelExcepciones;
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

    // Ruta para obtener todos los hoteles
    @Operation(summary = "Para obtener todos los hoteles", description = "Este endpoint devuelve una lista de todos los hoteles disponibles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de hoteles obtenida correctamente.")
    })
    @GetMapping("/hotels")
    public ResponseEntity<List<HotelDTO>> obtenerTodosLosHoteles() {
        List<HotelDTO> hoteles = hotelService.obtenerTodosLosHoteles();
        return ResponseEntity.status(200).body(hoteles);
    }

    // Ruta para obtener habitaciones disponibles en un rango de fechas y destino
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

    // Ruta para crear un nuevo hotel
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

    // Ruta para obtener un hotel por su ID
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

    // Ruta para reservar una habitación
    @Operation(summary = "Para reservar una habitación", description = "Este endpoint permite realizar una reserva de habitación en un hotel.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva realizada con éxito."),
            @ApiResponse(responseCode = "400", description = "Datos de la reserva incorrectos.")
    })
    @PostMapping("/room-booking/new")
    public ResponseEntity<ReservaHotelDTO> reservarHabitacion(@RequestBody ReservaHotelDTO reservaHotelDTO) {
        // Guardamos la reserva usando el servicio correspondiente
        ReservaHotelDTO reservaGuardada = reservaHotelService.guardarReserva(reservaHotelDTO);
        return ResponseEntity.status(201).body(reservaGuardada);
    }

    @Operation(summary = "Para editar un hotel", description = "Este endpoint permite editar los datos de un hotel existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotel editado correctamente."),
            @ApiResponse(responseCode = "400", description = "Datos de hotel incorrectos."),
            @ApiResponse(responseCode = "404", description = "Hotel no encontrado.")
    })
    @PutMapping("/hotels/edit/{id}")
    public ResponseEntity<HotelDTO> editarHotel(
            @Parameter(description = "ID del hotel a editar") @PathVariable Long id,
            @RequestBody HotelDTO hotelDTO) {
        try {
            HotelDTO hotelEditado = hotelService.editarHotel(id, hotelDTO);
            return ResponseEntity.status(200).body(hotelEditado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @Operation(summary = "Para eliminar un hotel por su ID",
            description = "Este endpoint permite eliminar un hotel específico de la base de datos utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotel eliminado con éxito."),
            @ApiResponse(responseCode = "400", description = "No se puede eliminar el hotel, ya que está reservado o en uso."),
            @ApiResponse(responseCode = "404", description = "Hotel no encontrado con el ID proporcionado.")
    })
    @DeleteMapping("/hotels/delete/{id}")
    public ResponseEntity<String> eliminarHotel(@Parameter(description = "ID del hotel a eliminar") @PathVariable Long id) {
        try {
            hotelService.eliminarHotel(id);
            return ResponseEntity.status(200).body("Hotel eliminado con éxito.");
        } catch (HotelExcepciones.HotelNoEncontradoException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (HotelExcepciones.HotelNoDisponibleException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }


}
