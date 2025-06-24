package com.testing.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.testing.entidades.Usuario;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
}
