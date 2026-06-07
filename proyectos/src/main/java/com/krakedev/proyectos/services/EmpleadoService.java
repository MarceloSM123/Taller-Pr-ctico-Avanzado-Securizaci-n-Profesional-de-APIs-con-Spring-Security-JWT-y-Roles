package com.krakedev.proyectos.services;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.krakedev.proyectos.entidades.Empleado;
import com.krakedev.proyectos.repositories.EmpleadoRepository;

@Service
public class EmpleadoService {
	 private final EmpleadoRepository empleadoRepository;

	    public EmpleadoService(EmpleadoRepository empleadoRepository) {
	        this.empleadoRepository = empleadoRepository;
	    }

	    public Empleado guardar(Empleado empleado) {
	        if (empleado == null) {
	            throw new RuntimeException("El empleado no puede ser nulo");
	        }

	        if (empleado.getId() != 0 && empleadoRepository.existsById(empleado.getId())) {
	            throw new RuntimeException("Ya existe un empleado con el ID: " + empleado.getId());
	        }
	        
	        return empleadoRepository.save(empleado);
	    }

	    public List<Empleado> listar() {
	        return empleadoRepository.findAll();
	    }

	    public Empleado buscar(int id) {
	        Optional<Empleado> resultado = empleadoRepository.findById(id);
	        return resultado.orElse(null);
	    }

	    public Empleado actualizar(int id, Empleado empleado) {
	        Empleado empleadoBuscado = buscar(id);
	        if (empleadoBuscado == null) {
	            return null;
	        }
	        empleadoBuscado.setNombre(empleado.getNombre());
	        empleadoBuscado.setCargo(empleado.getCargo());
	        empleadoBuscado.setTareas(empleado.getTareas());
	        return empleadoRepository.save(empleadoBuscado);
	    }

	    public boolean eliminar(int id) {
	        Empleado empleado = buscar(id);
	        if (empleado == null) {
	            return false;
	        }
	        empleadoRepository.deleteById(id);
	        return true;
	    }
}
