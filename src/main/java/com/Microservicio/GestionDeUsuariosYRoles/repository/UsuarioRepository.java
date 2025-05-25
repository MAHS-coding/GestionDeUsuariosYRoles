package com.Microservicio.GestionDeUsuariosYRoles.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Microservicio.GestionDeUsuariosYRoles.model.Rol;
import com.Microservicio.GestionDeUsuariosYRoles.model.TipoUsuario;
import com.Microservicio.GestionDeUsuariosYRoles.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    List<Usuario> findByTipoUsuario(TipoUsuario tipoUsuario);

    List<Usuario> findByActivo(boolean activo);

    List<Usuario> findByTipoUsuarioAndActivo(TipoUsuario tipoUsuario, boolean activo);

    List<Usuario> findByRol(Rol rol);
}
