package com.testing.integracion;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testing.controladores.UsuarioControlador;
import com.testing.entidades.Usuario;
import com.testing.servicios.UsuarioServicio;

@WebMvcTest(UsuarioControlador.class)
class IntegracionTopDownTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UsuarioServicio servicio;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crearYObtenerUsuario_TopDown() throws Exception {
        Usuario usuario = new Usuario("Juan", "juan@example.com");
        usuario.setId(1L);

        when(servicio.guardarUsuario(org.mockito.ArgumentMatchers.any(Usuario.class))).thenReturn(usuario);
        when(servicio.buscarUsuarioPorId(1L)).thenReturn(Optional.of(usuario));

        mvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"));

        mvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correo").value("juan@example.com"));
    }
}
