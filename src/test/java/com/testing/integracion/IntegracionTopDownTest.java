package com.testing.integracion;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import com.testing.controladores.UsuarioControlador;
import com.testing.entidades.Usuario;
import com.testing.servicios.UsuarioServicio;

/**
 * Pruebas de integración Top-Down.
 * Se parte del controlador y se simulan las capas inferiores.
 */
class IntegracionTopDownTest {

    /** Servicio falso para simular la lógica inferior */
    static class ServicioStub implements UsuarioServicio {
        private final Map<Long, Usuario> datos = new HashMap<>();
        private long secuencia = 1L;

        @Override
        public Usuario guardarUsuario(Usuario usuario) {
            usuario.setId(secuencia++);
            datos.put(usuario.getId(), usuario);
            return usuario;
        }

        @Override
        public Optional<Usuario> buscarUsuarioPorId(Long id) {
            return Optional.ofNullable(datos.get(id));
        }

        @Override
        public List<Usuario> buscarTodos() {
            return new ArrayList<>(datos.values());
        }

        @Override
        public void eliminarUsuario(Long id) {
            datos.remove(id);
        }

        @Override
        public Usuario actualizarUsuario(Long id, Usuario usuario) {
            Usuario existente = datos.get(id);
            if (existente == null) {
                throw new IllegalArgumentException("Usuario no encontrado");
            }
            existente.setNombre(usuario.getNombre());
            existente.setCorreo(usuario.getCorreo());
            return existente;
        }
    }

    // Paso 1: controlador usando un servicio simulado
    @Test
    void agregarUsuario_TopDown() {
        UsuarioControlador controlador = new UsuarioControlador(new ServicioStub());
        Usuario usuario = new Usuario("Juan", "juan@example.com");
        assertThat(controlador.crearUsuario(usuario).getBody().getId()).isNotNull();
    }

    // Paso 2: actualización usando el mismo servicio simulado
    @Test
    void modificarUsuario_TopDown() {
        ServicioStub stub = new ServicioStub();
        Usuario creado = stub.guardarUsuario(new Usuario("Juan", "juan@example.com"));
        UsuarioControlador controlador = new UsuarioControlador(stub);
        controlador.actualizarUsuario(creado.getId(), new Usuario("Juan Mod", "mod@example.com"));
        assertThat(controlador.obtenerUsuario(creado.getId()).getBody().getNombre()).contains("Mod");
    }

    // Paso 3: eliminar datos a través del controlador con el servicio falso
    @Test
    void eliminarUsuario_TopDown() {
        ServicioStub stub = new ServicioStub();
        Usuario creado = stub.guardarUsuario(new Usuario("Juan", "juan@example.com"));
        UsuarioControlador controlador = new UsuarioControlador(stub);
        controlador.eliminarUsuario(creado.getId());
        assertThat(controlador.obtenerUsuario(creado.getId()).getStatusCode().is4xxClientError()).isTrue();
    }
}
