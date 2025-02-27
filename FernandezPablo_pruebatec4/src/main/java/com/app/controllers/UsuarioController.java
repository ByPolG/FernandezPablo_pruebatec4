package com.app.controllers;

import com.app.dtos.UsuarioDTO;
import com.app.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agency/users")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Para obtener todos los usuarios", description = "Este endpoint devuelve una lista de todos los usuarios registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida correctamente."),
            @ApiResponse(responseCode = "403", description = "Acceso denegado.")
    })
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> obtenerTodosLosUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.obtenerTodosLosUsuarios();
        return ResponseEntity.status(200).body(usuarios);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    @Operation(summary = "Para obtener usuario por su ID", description = "Este endpoint devuelve un usuario en específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado."),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado."),
            @ApiResponse(responseCode = "403", description = "Acceso denegado.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorId(@PathVariable Long id) {
        UsuarioDTO usuario = usuarioService.obtenerUsuarioPorId(id);
        return usuario != null ? ResponseEntity.status(200).body(usuario)
                : ResponseEntity.status(404).build();
    }
}
