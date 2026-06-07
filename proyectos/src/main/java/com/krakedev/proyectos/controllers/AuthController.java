package com.krakedev.proyectos.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krakedev.proyectos.entidades.Usuario;
import com.krakedev.proyectos.repositories.UsuarioRepository;
import com.krakedev.proyectos.services.UsuarioService;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final UsuarioService usuarioService;
	private final UsuarioRepository usuarioRepository;
	
	public AuthController(UsuarioService usuarioService, UsuarioRepository usuarioRepository) {
		super();
		this.usuarioService = usuarioService;
		this.usuarioRepository = usuarioRepository;
	}



	@PostMapping("/registrar")
	public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
	    try {
	        Usuario usuarioNuevo = usuarioService.guardar(usuario);
	        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error al registrar usuario: " + e.getMessage());
	    }
	}
}
