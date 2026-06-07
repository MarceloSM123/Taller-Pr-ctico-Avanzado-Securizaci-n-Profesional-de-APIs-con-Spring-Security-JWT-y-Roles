package com.krakedev.proyectos.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.krakedev.proyectos.entidades.Tarea;
import com.krakedev.proyectos.entidades.Proyecto;
import com.krakedev.proyectos.entidades.Empleado;
import com.krakedev.proyectos.repositories.TareaRepository;
import com.krakedev.proyectos.repositories.ProyectoRepository;
import com.krakedev.proyectos.repositories.EmpleadoRepository;

@Service
public class TareaService {
	private final TareaRepository tareaRepository;
	private final ProyectoRepository proyectoRepository;
	private final EmpleadoRepository empleadoRepository;

	public TareaService(TareaRepository tareaRepository, ProyectoRepository proyectoRepository,
			EmpleadoRepository empleadoRepository) {
		this.tareaRepository = tareaRepository;
		this.proyectoRepository = proyectoRepository;
		this.empleadoRepository = empleadoRepository;
	}

	public Tarea guardar(Tarea tarea) {
		Proyecto proyecto = proyectoRepository.findById(tarea.getProyecto().getId())
				.orElseThrow(() -> new RuntimeException("Proyecto no existe"));


		List<Empleado> empleadosDB = new ArrayList<>();
		if (tarea.getEmpleados() != null) {
			for (Empleado empleado : tarea.getEmpleados()) {
				Empleado empleadoReal = empleadoRepository.findById(empleado.getId())
						.orElseThrow(() -> new RuntimeException("Empleado con ID " + empleado.getId() + " no existe"));
				empleadosDB.add(empleadoReal);
			}
		}

		tarea.setProyecto(proyecto);
		tarea.setEmpleados(empleadosDB);

		return tareaRepository.save(tarea);
	}

	public List<Tarea> listar() {
		return tareaRepository.findAll();
	}

	public Tarea buscar(int id) {
		Optional<Tarea> resultado = tareaRepository.findById(id);
		return resultado.orElse(null);
	}

	public Tarea actualizar(int id, Tarea tarea) {
		Tarea tareaBuscada = buscar(id);
		if (tareaBuscada == null) {
			return null;
		}


		if (tarea.getProyecto() != null && tarea.getProyecto().getId() != 0) {
			Proyecto proyecto = proyectoRepository.findById(tarea.getProyecto().getId())
					.orElseThrow(() -> new RuntimeException("Proyecto no existe"));
			tareaBuscada.setProyecto(proyecto);
		}

		if (tarea.getEmpleados() != null && !tarea.getEmpleados().isEmpty()) {
			List<Empleado> empleadosDB = new ArrayList<>();
			for (Empleado empleado : tarea.getEmpleados()) {
				Empleado empleadoReal = empleadoRepository.findById(empleado.getId())
						.orElseThrow(() -> new RuntimeException("Empleado con ID " + empleado.getId() + " no existe"));
				empleadosDB.add(empleadoReal);
			}
			tareaBuscada.setEmpleados(empleadosDB);
		}

		tareaBuscada.setDescripcion(tarea.getDescripcion());
		tareaBuscada.setFechaLimite(tarea.getFechaLimite());
		tareaBuscada.setCostoEstimado(tarea.getCostoEstimado());

		return tareaRepository.save(tareaBuscada);
	}

	public boolean eliminar(int id) {
		Tarea tarea = buscar(id);
		if (tarea == null) {
			return false;
		}
		tareaRepository.deleteById(id);
		return true;
	}
}
