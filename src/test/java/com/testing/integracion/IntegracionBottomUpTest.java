package com.testing.integracion;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.testing.entidades.Usuario;
import com.testing.servicios.UsuarioServicio;
import com.testing.controladores.UsuarioControlador;
import com.testing.repositorios.UsuarioRepositorio;

@SpringBootTest
@Transactional
class IntegracionBottomUpTest {

    @Autowired
    private UsuarioRepositorio repositorio;

    @Autowired
    private UsuarioServicio servicio;

    @Autowired
    private UsuarioControlador controlador;

    // Paso 1: probar la capa de repositorio
    @Test
    void agregarUsuario_BottomUp() {
        Usuario usuario = repositorio.save(new Usuario("Ana", "ana@example.com"));
        assertThat(repositorio.findById(usuario.getId())).isPresent();
    }

    // Paso 2: integrar servicio y repositorio
    @Test
    void modificarUsuario_BottomUp() {
        Usuario usuario = repositorio.save(new Usuario("Ana", "ana@example.com"));
        Usuario actualizado = servicio.actualizarUsuario(usuario.getId(), new Usuario("Ana Mod", "anamod@example.com"));
        assertThat(actualizado.getNombre()).contains("Mod");
    }

    // Paso 3: integrar controlador con el resto de capas
    @Test
    void eliminarUsuario_BottomUp() {
        Usuario usuario = repositorio.save(new Usuario("Ana", "ana@example.com"));
        controlador.eliminarUsuario(usuario.getId());
        assertThat(repositorio.findById(usuario.getId())).isEmpty();
    }
}
