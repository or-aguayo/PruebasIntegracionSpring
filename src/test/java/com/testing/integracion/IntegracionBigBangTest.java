package com.testing.integracion;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.testing.entidades.Usuario;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegracionBigBangTest {


    @Autowired
    private TestRestTemplate restTemplate;

    // Enfoque Big Bang: todas las capas se integran y se prueba el endpoint real
    @Test
    void agregarUsuario_BigBang() {
        Usuario usuario = new Usuario("Luis", "luis@example.com");
        ResponseEntity<Usuario> respuesta = restTemplate.postForEntity("/usuarios", usuario, Usuario.class);
        assertThat(respuesta.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(respuesta.getBody().getId()).isNotNull();
    }

    // Enfoque Big Bang: actualización a través de la API integrada
    @Test
    void modificarUsuario_BigBang() {
        Usuario usuario = new Usuario("Luis", "luis@example.com");
        ResponseEntity<Usuario> creacion = restTemplate.postForEntity("/usuarios", usuario, Usuario.class);
        Long id = creacion.getBody().getId();

        Usuario actualizado = new Usuario("Luis Mod", "luismod@example.com");
        restTemplate.exchange("/usuarios/" + id, HttpMethod.PUT, new HttpEntity<>(actualizado), Usuario.class);
        ResponseEntity<Usuario> respuesta = restTemplate.getForEntity("/usuarios/" + id, Usuario.class);
        assertThat(respuesta.getBody().getNombre()).contains("Mod");
    }

    // Enfoque Big Bang: eliminación usando todo el flujo HTTP
    @Test
    void eliminarUsuario_BigBang() {
        Usuario usuario = new Usuario("Luis", "luis@example.com");
        ResponseEntity<Usuario> creacion = restTemplate.postForEntity("/usuarios", usuario, Usuario.class);
        Long id = creacion.getBody().getId();

        restTemplate.delete("/usuarios/" + id);
        ResponseEntity<Usuario> respuesta = restTemplate.getForEntity("/usuarios/" + id, Usuario.class);
        assertThat(respuesta.getStatusCode().is4xxClientError()).isTrue();
    }
}
