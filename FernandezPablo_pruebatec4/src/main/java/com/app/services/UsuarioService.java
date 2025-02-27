package com.app.services;

import com.app.dtos.UsuarioDTO;
import com.app.entities.Usuario;
import com.app.repositories.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements UsuarioServiceInterfaz {

    @Autowired
    private UsuarioRepositorio usuarioRepository;

    // Obtenemos todos los usuarios
    @Override
    public List<UsuarioDTO> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(usuario -> new UsuarioDTO(
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getContrasenia(),  // Ahora también pasas la contraseña
                        usuario.getRol().name(),  // El rol como String
                        null,  // Asignar null a las listas de reservas si no las tienes
                        null   // Asignar null a las listas de reservas si no las tienes
                ))
                .collect(Collectors.toList());
    }

    // Obtenemos un usuario por su ID
    @Override
    public UsuarioDTO obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getContrasenia(),
                usuario.getRol().name(),
                null,  // Asignamos null a las listas de reservas si no las tienes
                null   // Asignamos null a las listas de reservas si no las tienes
        );
    }
}
