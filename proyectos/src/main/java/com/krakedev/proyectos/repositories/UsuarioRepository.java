package com.krakedev.proyectos.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krakedev.proyectos.entidades.Tarea;
import com.krakedev.proyectos.entidades.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
	// Método para buscar un usuario por username
    Optional<Usuario> findByUsername(String username);
    
    // Método para verificar si existe un username
    boolean existsByUsername(String username);
}
