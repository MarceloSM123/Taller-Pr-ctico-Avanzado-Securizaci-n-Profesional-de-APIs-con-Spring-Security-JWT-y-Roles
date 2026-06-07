package com.krakedev.proyectos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.krakedev.proyectos.entidades.Proyecto;
import com.krakedev.proyectos.repositories.ProyectoRepository;

@Service
public class ProyectoService {
	private final ProyectoRepository proyectoRepository;

	public ProyectoService(ProyectoRepository proyectoRepository) {
		this.proyectoRepository = proyectoRepository;
	}

	public Proyecto guardar(Proyecto proyecto) {
		if (proyecto == null) {
			throw new RuntimeException("El proyecto no puede ser nulo");
		}

		if (proyecto.getId() != 0 && proyectoRepository.existsById(proyecto.getId())) {
			throw new RuntimeException("Ya existe un proyecto con el ID: " + proyecto.getId());
		}

		return proyectoRepository.save(proyecto);
	}

	public List<Proyecto> listar() {
		return proyectoRepository.findAll();
	}

	public Proyecto buscar(int id) {
		Optional<Proyecto> resultado = proyectoRepository.findById(id);
		return resultado.orElse(null);
	}

	public Proyecto actualizar(int id, Proyecto proyecto) {
		Proyecto proyectoBuscado = buscar(id);
		if (proyectoBuscado == null) {
			return null;
		}
		proyectoBuscado.setNombre(proyecto.getNombre());
		proyectoBuscado.setDescripcion(proyecto.getDescripcion());
		proyectoBuscado.setFechaInicio(proyecto.getFechaInicio());
		proyectoBuscado.setTareas(proyecto.getTareas());
		return proyectoRepository.save(proyectoBuscado);
	}

	public boolean eliminar(int id) {
		Proyecto proyecto = buscar(id);
		if (proyecto == null) {
			return false;
		}
		proyectoRepository.deleteById(id);
		return true;
	}
}
