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
class IntegracionTopDownTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void flujoIncremental_TopDown() {
        Usuario usuario = new Usuario("Juan", "juan@example.com");
        ResponseEntity<Usuario> respuestaCreacion = restTemplate.postForEntity("/usuarios", usuario, Usuario.class);
        assertThat(respuestaCreacion.getStatusCode().is2xxSuccessful()).isTrue();

        Long id = respuestaCreacion.getBody().getId();
        assertThat(id).isNotNull();

        Usuario actualizado = new Usuario("Juan Mod", "juan.mod@example.com");
        restTemplate.exchange("/usuarios/" + id, HttpMethod.PUT, new HttpEntity<>(actualizado), Usuario.class);

        ResponseEntity<Usuario> respuestaConsulta = restTemplate.getForEntity("/usuarios/" + id, Usuario.class);
        assertThat(respuestaConsulta.getBody().getNombre()).contains("Mod");

        restTemplate.delete("/usuarios/" + id);
        ResponseEntity<Usuario> respuestaBorrado = restTemplate.getForEntity("/usuarios/" + id, Usuario.class);
        assertThat(respuestaBorrado.getStatusCode().is4xxClientError()).isTrue();
    }
}
