package com.testing.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.testing.entidades.Usuario;
import com.testing.repositorios.UsuarioRepositorio;

@Service
@Transactional
public class UsuarioServicioImpl implements UsuarioServicio {
    private final UsuarioRepositorio repositorio;

    public UsuarioServicioImpl(UsuarioRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Usuario guardarUsuario(Usuario usuario) {
        return repositorio.save(usuario);
    }

    @Override
    public Optional<Usuario> buscarUsuarioPorId(Long id) {
        return repositorio.findById(id);
    }

    @Override
    public List<Usuario> buscarTodos() {
        return repositorio.findAll();
    }

    @Override
    public void eliminarUsuario(Long id) {
        repositorio.deleteById(id);
    }

    @Override
    public Usuario actualizarUsuario(Long id, Usuario usuario) {
        return repositorio.findById(id)
                .map(u -> {
                    u.setNombre(usuario.getNombre());
                    u.setCorreo(usuario.getCorreo());
                    return repositorio.save(u);
                })
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }
}
