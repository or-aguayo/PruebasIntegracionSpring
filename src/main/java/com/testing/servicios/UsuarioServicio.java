package com.testing.servicios;

import java.util.List;
import java.util.Optional;

import com.testing.entidades.Usuario;

public interface UsuarioServicio {
    Usuario guardarUsuario(Usuario usuario);

    Optional<Usuario> buscarUsuarioPorId(Long id);

    List<Usuario> buscarTodos();

    void eliminarUsuario(Long id);

    Usuario actualizarUsuario(Long id, Usuario usuario);
}
