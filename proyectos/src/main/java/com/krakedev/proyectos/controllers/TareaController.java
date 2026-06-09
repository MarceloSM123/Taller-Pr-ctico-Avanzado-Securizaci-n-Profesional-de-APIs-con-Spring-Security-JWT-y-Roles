package com.krakedev.proyectos.controllers;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krakedev.proyectos.entidades.Tarea;
import com.krakedev.proyectos.services.TareaService;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private final TareaService tareaService;
    
    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<?> guardar(@RequestBody Tarea tarea) {
        try {
            Tarea nuevaTarea = tareaService.guardar(tarea);
            return new ResponseEntity<>(nuevaTarea, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor: " + e.getMessage(), 
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/")
    public ResponseEntity<?> listar() {
        try {
            List<Tarea> tareas = tareaService.listar();
            return new ResponseEntity<>(tareas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al listar tareas: " + e.getMessage(), 
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable int id) {
        try {
            Tarea tarea = tareaService.buscar(id);
            if (tarea == null) {
                return new ResponseEntity<>("Tarea no encontrada con ID: " + id, 
                        HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(tarea, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al buscar tarea: " + e.getMessage(), 
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable int id, @RequestBody Tarea tarea) {
        try {
            Tarea tareaActualizada = tareaService.actualizar(id, tarea);
            if (tareaActualizada == null) {
                return new ResponseEntity<>("Tarea no encontrada con ID: " + id, 
                        HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(tareaActualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al actualizar tarea: " + e.getMessage(), 
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable int id) {
        try {
            boolean eliminado = tareaService.eliminar(id);
            if (!eliminado) {
                return new ResponseEntity<>("Tarea no encontrada con ID: " + id, 
                        HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>("Tarea eliminada correctamente", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar tarea: " + e.getMessage(), 
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
