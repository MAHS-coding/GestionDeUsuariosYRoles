package com.Microservicio.GestionDeUsuariosYRoles.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Microservicio.GestionDeUsuariosYRoles.model.AlumnoCursoAceptado;
import com.Microservicio.GestionDeUsuariosYRoles.repository.AlumnoCursoAceptadoRepository;

@Service
public class AlumnoCursoAceptadoService {

    @Autowired
    private AlumnoCursoAceptadoRepository repository;

    public AlumnoCursoAceptadoService(AlumnoCursoAceptadoRepository repository) {
        this.repository = repository;
    }

    public boolean existeAlumnoCurso(int idUsuario, Long idCurso) {
        return repository.findByIdUsuarioAndIdCurso(idUsuario, idCurso).isPresent();
    }

    public AlumnoCursoAceptado registrarAlumnoCurso(int idUsuario, Long idCurso) {
        if (existeAlumnoCurso(idUsuario, idCurso)) {
            throw new RuntimeException("Usuario ya está inscrito en el curso");
        }
        AlumnoCursoAceptado nuevo = new AlumnoCursoAceptado(null, idUsuario, idCurso);
        return repository.save(nuevo);
    }

    public List<AlumnoCursoAceptado> obtenerCursosPorUsuario(int idUsuario) {
        return repository.findByIdUsuario(idUsuario);
    }

}
