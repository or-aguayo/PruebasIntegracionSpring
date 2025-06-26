package com.testing.vistas;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.testing.entidades.Usuario;
import com.testing.servicios.UsuarioServicio;

/**
 * Controlador MVC para las vistas en Thymeleaf.
 */
@Controller
public class UsuarioVistaControlador {

    private final UsuarioServicio servicio;

    public UsuarioVistaControlador(UsuarioServicio servicio) {
        this.servicio = servicio;
    }

    /**
     * Muestra el listado de usuarios.
     */
    @GetMapping("/v/usuarios")
    public String listar(Model model) {
        List<Usuario> usuarios = servicio.buscarTodos();
        model.addAttribute("usuarios", usuarios);
        return "usuarios/listado";
    }

    /**
     * Muestra el formulario para crear un usuario.
     */
    @GetMapping("/v/usuarios/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/formulario";
    }

    /**
     * Guarda el nuevo usuario.
     */
    @PostMapping("/v/usuarios/guardar")
    public String guardar(@ModelAttribute Usuario usuario) {
        servicio.guardarUsuario(usuario);
        return "redirect:/v/usuarios";
    }

    /**
     * Muestra el formulario para editar un usuario existente.
     */
    @GetMapping("/v/usuarios/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        servicio.buscarUsuarioPorId(id).ifPresent(u -> model.addAttribute("usuario", u));
        return "usuarios/formulario";
    }

    /**
     * Elimina un usuario por id y vuelve al listado.
     */
    @GetMapping("/v/usuarios/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        servicio.eliminarUsuario(id);
        return "redirect:/v/usuarios";
    }
}
