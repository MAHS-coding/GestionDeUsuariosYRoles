package com.Microservicio.GestionDeUsuariosYRoles.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Microservicio.GestionDeUsuariosYRoles.model.Rol;
import com.Microservicio.GestionDeUsuariosYRoles.repository.RolRepository;

@Service
public class RolService {
    @Autowired
    private RolRepository rolRepository;

    public List<Rol> listarTodosLosRoles() {
        return rolRepository.findAll();
    }

    public List<Rol> listarRolesPorEstado(boolean activo) {
        return rolRepository.findByActivo(activo);
    }

    // Crear un nuevo rol
    public Rol crearRol(Rol rol) {
        // Validación básica
        if (rol.getNombre() == null || rol.getNombre().isEmpty()) {
            throw new RuntimeException("El nombre del rol es requerido");
        }
        if (rol.getPermisos() == null || rol.getPermisos().isEmpty()) {
            throw new RuntimeException("Los permisos son requeridos");
        }

        return rolRepository.save(rol);
    }

    // Editar permisos
    public Rol editarPermisos(Long idRol, Set<String> nuevosPermisos) {
        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        rol.setPermisos(nuevosPermisos);
        return rolRepository.save(rol);
    }

    // Consultar permisos
    public Set<String> consultarPermisos(Long idRol) {
        return rolRepository.findById(idRol)
                .map(Rol::getPermisos)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
    }

    // Bloquar/Desbloquear rol
    public Rol cambiarEstadoRol(Long idRol, boolean estado) {
        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        rol.setActivo(estado);
        return rolRepository.save(rol);
    }

    // Eliminar rol
    public void eliminarRol(Long idRol) {
        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        rolRepository.delete(rol);
    }
}
