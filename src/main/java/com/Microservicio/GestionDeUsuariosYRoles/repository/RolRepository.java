package com.Microservicio.GestionDeUsuariosYRoles.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Microservicio.GestionDeUsuariosYRoles.model.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre);

    List<Rol> findByActivo(boolean activo);
}
