package com.krakedev.proyectos.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.krakedev.proyectos.entidades.Usuario;
import com.krakedev.proyectos.repositories.UsuarioRepository;
import com.krakedev.proyectos.security.JwtUtil;
import com.krakedev.proyectos.services.TokenBlackListService;
import com.krakedev.proyectos.services.UsuarioService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final UsuarioService usuarioService;
	private final UsuarioRepository usuarioRepository;
	private final TokenBlackListService blackListService;

	public AuthController(UsuarioService usuarioService, UsuarioRepository usuarioRepository,TokenBlackListService blackListService) {
		super();
		this.usuarioService = usuarioService;
		this.usuarioRepository = usuarioRepository;
		this.blackListService=blackListService;
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

	 @PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
	    String username = credenciales.get("username");
	    String password = credenciales.get("password");
	    
	    boolean autenticado = usuarioService.autenticar(username, password);
	    
	    if (autenticado) {
	        Usuario usuario = usuarioRepository.findByUsername(username).get();
	        String token = JwtUtil.generarToken(usuario.getUsername(),usuario.getRol());
	        return ResponseEntity.ok(Map.of("Token: ",token));
	    } else {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrecta");
	    }
	}
	 
	@GetMapping("/perfil")
	/* public ResponseEntity<?> verPerfil(@RequestHeader(value = "Authorization", required = false) String authHeader) {

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body("Acceso Denegado: Debes proveer un token Bearer valido en la cabecera Authorization");

		}

		String token = authHeader.substring(7);

		DecodedJWT datosToken = JwtUtil.validarToken(token);

		if (datosToken == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso Denegado: Token Inválido o Expirado");
		}

		String usuario = datosToken.getSubject();

		String rol = datosToken.getClaim("rol").asString();

		return ResponseEntity.ok(Map.of("Mensaje", "Bienvenido al sistema protegido por jwt", "Usuario", usuario, "Rol",
				rol, "Estatus", "Autenticado Exitosamente"));
	}*/
	public ResponseEntity<?> verPerfil() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		String usuario = auth.getName();

		String rol = auth.getAuthorities().iterator().next().getAuthority();

		return ResponseEntity.ok(Map.of("Mensaje", "Bienvenido al sistema protegido por Spring Security", 
				"Usuario", usuario, 
				"rol_detectado", rol, 
				"status", "Autenticado Exitosamente"));
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			blackListService.invalidarToken(token);
			return ResponseEntity.ok(Map.of("mensaje", "Sesión cerrada exitosamente. Token invalidado"));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token no proporcionado");
		}
	} 
}
