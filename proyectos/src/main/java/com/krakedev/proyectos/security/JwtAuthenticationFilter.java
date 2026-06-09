package com.krakedev.proyectos.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.krakedev.proyectos.services.TokenBlackListService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
// @Component crea una instancia de forma automatica al iniciar la aplicacion 
public class JwtAuthenticationFilter extends OncePerRequestFilter { // no crear como abstracta, hay que asegurarse de
																	// sobreescribir los metodos
	// OncePerRequestFilter filtro que se ejecuta una vez por peticion
	private final TokenBlackListService blackListService;

// inyeccion por el contructor
	public JwtAuthenticationFilter(TokenBlackListService blackListService) {
		super();
		this.blackListService = blackListService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response /* lo que el servidor le devuelve al cliente */, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeder = request.getHeader("Authorization");

		if (authHeder == null || !authHeder.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
		}

		String token = authHeder.substring(7);

		if (blackListService.estaInvalidado(token)) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Acceso denegado: Session Cerrada");
			return;
		}

		DecodedJWT datosToken = JwtUtil.validarToken(token);//

		if (datosToken != null) {
			String username = datosToken.getSubject();
			String rolOriginal = datosToken.getClaim("rol").asString();
			String rolSpring = "ROLE_" + rolOriginal;
			SimpleGrantedAuthority authority = new SimpleGrantedAuthority(rolSpring);
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null,
					Collections.singleton(authority));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		filterChain.doFilter(request, response);
	}

}
