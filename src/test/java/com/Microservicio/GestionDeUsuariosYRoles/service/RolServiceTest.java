package com.Microservicio.GestionDeUsuariosYRoles.service;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import com.Microservicio.GestionDeUsuariosYRoles.model.Rol;
import com.Microservicio.GestionDeUsuariosYRoles.repository.RolRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class RolServiceTest {

    @Mock
    private RolRepository repository;

    @InjectMocks
    private RolService service;

    private Rol crearRol(Long id, String nombre, boolean activo, Set<String> permisos) {
        Rol rol = new Rol();
        rol.setId(id);
        rol.setNombre(nombre);
        rol.setActivo(activo);
        rol.setPermisos(permisos);
        return rol;
    }

    @Test
    void testCrearRol_Exitoso() {
        // Configuración
        Set<String> permisos = new HashSet<>(Arrays.asList("permiso1", "permiso2"));
        Rol rol = crearRol(null, "Admin", true, permisos);
        Rol guardado = crearRol(1L, "Admin", true, permisos);
        
        when(repository.save(rol)).thenReturn(guardado);
        
        // Ejecución
        Rol resultado = service.crearRol(rol);
        
        // Verificación
        assertThat(resultado.getId()).isEqualTo(1L);
        verify(repository).save(rol);
    }

    @Test
    void testCrearRol_SinNombre() {
        // Configuración
        Rol rol = crearRol(null, null, true, Collections.singleton("permiso"));
        
        // Verificación de excepción
        assertThatThrownBy(() -> service.crearRol(rol))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("El nombre del rol es requerido");
        
        verify(repository, never()).save(any());
    }

    @Test
    void testEditarPermisos() {
        // Configuración
        Rol existente = crearRol(1L, "Admin", true, new HashSet<>());
        Set<String> nuevosPermisos = new HashSet<>(Arrays.asList("nuevo1", "nuevo2"));
        
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);
        
        // Ejecución
        Rol resultado = service.editarPermisos(1L, nuevosPermisos);
        
        // Verificación
        assertThat(resultado.getPermisos()).isEqualTo(nuevosPermisos);
        verify(repository).save(existente);
    }

    @Test
    void testConsultarPermisos() {
        // Configuración
        Set<String> permisos = new HashSet<>(Arrays.asList("p1", "p2"));
        Rol rol = crearRol(1L, "Admin", true, permisos);
        
        when(repository.findById(1L)).thenReturn(Optional.of(rol));
        
        // Ejecución
        Set<String> resultado = service.consultarPermisos(1L);
        
        // Verificación
        assertThat(resultado).isEqualTo(permisos);
        verify(repository).findById(1L);
    }

    @Test
    void testCambiarEstadoRol() {
        // Configuración
        Rol rol = crearRol(1L, "Admin", true, new HashSet<>());
        
        when(repository.findById(1L)).thenReturn(Optional.of(rol));
        when(repository.save(rol)).thenReturn(rol);
        
        // Ejecución
        Rol resultado = service.cambiarEstadoRol(1L, false);
        
        // Verificación
        assertThat(resultado.isActivo()).isFalse();
        verify(repository).save(rol);
    }

    @Test
    void testEliminarRol() {
        // Configuración
        Rol rol = crearRol(1L, "Admin", true, new HashSet<>());
        
        when(repository.findById(1L)).thenReturn(Optional.of(rol));
        doNothing().when(repository).delete(rol);
        
        // Ejecución
        service.eliminarRol(1L);
        
        // Verificación
        verify(repository).delete(rol);
    }
}