package com.Microservicio.GestionDeUsuariosYRoles.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Microservicio.GestionDeUsuariosYRoles.model.AlumnoCursoAceptado;

@Repository
public interface AlumnoCursoAceptadoRepository extends JpaRepository<AlumnoCursoAceptado, Long> {
    Optional<AlumnoCursoAceptado> findByIdUsuarioAndIdCurso(int idUsuario, Long idCurso);

    List<AlumnoCursoAceptado> findByIdUsuario(int idUsuario);

}
