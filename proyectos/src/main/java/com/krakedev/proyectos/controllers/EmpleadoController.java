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

import com.krakedev.proyectos.entidades.Empleado;
import com.krakedev.proyectos.services.EmpleadoService;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

	private final EmpleadoService empleadoService;

	public EmpleadoController(EmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}

	@PostMapping("/")
	public ResponseEntity<?> guardar(@RequestBody Empleado empleado) {
		try {
			Empleado nuevoEmpleado = empleadoService.guardar(empleado);
			return new ResponseEntity<>(nuevoEmpleado, HttpStatus.CREATED);
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
			List<Empleado> empleados = empleadoService.listar();
			return new ResponseEntity<>(empleados, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error al listar empleados: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> buscar(@PathVariable int id) {
		try {
			Empleado empleado = empleadoService.buscar(id);
			if (empleado == null) {
				return new ResponseEntity<>("Empleado no encontrado con ID: " + id, HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(empleado, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error al buscar empleado: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> actualizar(@PathVariable int id, @RequestBody Empleado empleado) {
		try {
			Empleado empleadoActualizado = empleadoService.actualizar(id, empleado);
			if (empleadoActualizado == null) {
				return new ResponseEntity<>("Empleado no encontrado con ID: " + id, HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(empleadoActualizado, HttpStatus.OK);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>("Error al actualizar empleado: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) {
		try {
			boolean eliminado = empleadoService.eliminar(id);
			if (!eliminado) {
				return new ResponseEntity<>("Empleado no encontrado con ID: " + id, HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>("Empleado eliminado correctamente", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error al eliminar empleado: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
