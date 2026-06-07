package com.krakedev.proyectos.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krakedev.proyectos.entidades.Proyecto;
import com.krakedev.proyectos.services.ProyectoService;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {
	private final ProyectoService proyectoService;

	public ProyectoController(ProyectoService proyectoService) {
		this.proyectoService = proyectoService;
	}

	@PostMapping("/")
	public ResponseEntity<?> guardar(@RequestBody Proyecto proyecto) {
		try {
			Proyecto nuevoProyecto = proyectoService.guardar(proyecto);
			return new ResponseEntity<>(nuevoProyecto, HttpStatus.CREATED);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>("Error interno del servidor: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/")
	public ResponseEntity<?> listar() {
		try {
			List<Proyecto> proyectos = proyectoService.listar();
			return new ResponseEntity<>(proyectos, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error al listar proyectos: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> buscar(@PathVariable int id) {
		try {
			Proyecto proyecto = proyectoService.buscar(id);
			if (proyecto == null) {
				return new ResponseEntity<>("Proyecto no encontrado con ID: " + id, HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(proyecto, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error al buscar proyecto: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> actualizar(@PathVariable int id, @RequestBody Proyecto proyecto) {
		try {
			Proyecto proyectoActualizado = proyectoService.actualizar(id, proyecto);
			if (proyectoActualizado == null) {
				return new ResponseEntity<>("Proyecto no encontrado con ID: " + id, HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(proyectoActualizado, HttpStatus.OK);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>("Error al actualizar proyecto: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) {
		try {
			boolean eliminado = proyectoService.eliminar(id);
			if (!eliminado) {
				return new ResponseEntity<>("Proyecto no encontrado con ID: " + id, HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>("Proyecto eliminado correctamente", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error al eliminar proyecto: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
