package com.testing.integracion;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.testing.entidades.Usuario;
import com.testing.servicios.UsuarioServicio;

@SpringBootTest
@Transactional
class IntegracionBottomUpTest {

    @Autowired
    private UsuarioServicio servicio;

    @Test
    void guardarYObtenerUsuario_BottomUp() {
        Usuario usuario = new Usuario("Ana", "ana@example.com");
        Usuario guardado = servicio.guardarUsuario(usuario);

        assertThat(guardado.getId()).isNotNull();
        assertThat(servicio.buscarUsuarioPorId(guardado.getId())).isPresent();
    }
}
