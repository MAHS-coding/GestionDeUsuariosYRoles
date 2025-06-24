package com.Microservicio.GestionDeUsuariosYRoles.service;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import com.Microservicio.GestionDeUsuariosYRoles.model.AlumnoCursoAceptado;
import com.Microservicio.GestionDeUsuariosYRoles.repository.AlumnoCursoAceptadoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AlumnoCursoAceptadoServiceTest {

    @Mock
    private AlumnoCursoAceptadoRepository repository;

    @InjectMocks
    private AlumnoCursoAceptadoService service;

    private AlumnoCursoAceptado crearVinculacion(Long id, int idUsuario, Long idCurso) {
        AlumnoCursoAceptado vinculacion = new AlumnoCursoAceptado();
        vinculacion.setIdCurso(id);
        vinculacion.setIdUsuario(idUsuario);
        vinculacion.setIdCurso(idCurso);
        return vinculacion;
    }

    @Test
    void testExisteAlumnoCurso_Existente() {
        // Configuración
        AlumnoCursoAceptado existente = crearVinculacion(1L, 1, 100L);
        when(repository.findByIdUsuarioAndIdCurso(1, 100L))
            .thenReturn(Optional.of(existente));
        
        // Ejecución
        boolean resultado = service.existeAlumnoCurso(1, 100L);
        
        // Verificación
        assertThat(resultado).isTrue();
        verify(repository).findByIdUsuarioAndIdCurso(1, 100L);
    }

    @Test
    void testExisteAlumnoCurso_NoExistente() {
        when(repository.findByIdUsuarioAndIdCurso(1, 100L))
            .thenReturn(Optional.empty());
        
        boolean resultado = service.existeAlumnoCurso(1, 100L);
        
        assertThat(resultado).isFalse();
        verify(repository).findByIdUsuarioAndIdCurso(1, 100L);
    }

    @Test
    void testRegistrarAlumnoCurso_Exitoso() {
        // Configuración
        when(repository.findByIdUsuarioAndIdCurso(1, 100L))
            .thenReturn(Optional.empty());
        
        AlumnoCursoAceptado nuevo = crearVinculacion(null, 1, 100L);
        AlumnoCursoAceptado guardado = crearVinculacion(1L, 1, 100L);
        
        when(repository.save(nuevo)).thenReturn(guardado);
        
        // Ejecución
        AlumnoCursoAceptado resultado = service.registrarAlumnoCurso(1, 100L);
        
        // Verificación
        assertThat(resultado.getIdCurso()).isEqualTo(100L);
        verify(repository).save(nuevo);
    }

    @Test
    void testRegistrarAlumnoCurso_Duplicado() {
        // Configuración
        AlumnoCursoAceptado existente = crearVinculacion(1L, 1, 100L);
        when(repository.findByIdUsuarioAndIdCurso(1, 100L))
            .thenReturn(Optional.of(existente));
        
        // Verificación de excepción
        assertThatThrownBy(() -> service.registrarAlumnoCurso(1, 100L))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Usuario ya está inscrito en el curso");
        
        verify(repository, never()).save(any());
    }

    @Test
    void testObtenerCursosPorUsuario() {
        // Configuración
        List<AlumnoCursoAceptado> cursos = Arrays.asList(
            crearVinculacion(1L, 1, 100L),
            crearVinculacion(2L, 1, 200L)
        );
        
        when(repository.findByIdUsuario(1)).thenReturn(cursos);
        
        // Ejecución
        List<AlumnoCursoAceptado> resultado = service.obtenerCursosPorUsuario(1);
        
        // Verificación
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getIdCurso()).isEqualTo(100L);
        assertThat(resultado.get(1).getIdCurso()).isEqualTo(200L);
        verify(repository).findByIdUsuario(1);
    }
}