package com.app.services;

import com.app.dtos.UsuarioDTO;
import java.util.List;

public interface UsuarioServiceInterfaz {

    List<UsuarioDTO> obtenerTodosLosUsuarios();

    UsuarioDTO obtenerUsuarioPorId(Long id);
}
