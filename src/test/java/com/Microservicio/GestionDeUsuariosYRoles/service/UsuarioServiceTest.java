package com.Microservicio.GestionDeUsuariosYRoles.service;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import com.Microservicio.GestionDeUsuariosYRoles.model.AlumnoCursoAceptado;
import com.Microservicio.GestionDeUsuariosYRoles.model.Rol;
import com.Microservicio.GestionDeUsuariosYRoles.model.TipoUsuario;
import com.Microservicio.GestionDeUsuariosYRoles.model.Usuario;
import com.Microservicio.GestionDeUsuariosYRoles.repository.RolRepository;
import com.Microservicio.GestionDeUsuariosYRoles.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    
    @Mock
    private RolRepository rolRepository;
    
    @Mock
    private AlumnoCursoAceptadoService cursoAceptadoService;
    
    @InjectMocks
    private UsuarioService service;

    private Usuario crearUsuario(int idUsuario, String nombre, TipoUsuario tipoUsuario, boolean activo) {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(idUsuario);
        usuario.setNombreUsuario(nombre);
        usuario.setTipoUsuario(tipoUsuario);
        usuario.setActivo(activo);
        return usuario;
    }

    private Rol crearRol(Long id, String nombre) {
        Rol rol = new Rol();
        rol.setId(id);
        rol.setNombre(nombre);
        return rol;
    }

    @Test
    void testCrearUsuario_Exitoso() {
        // Configuración
        Usuario usuario = crearUsuario(0, "Juan", TipoUsuario.ESTUDIANTE, true);
        usuario.setApellidoPUsuario("Perez");
        usuario.setApellidoMUsuario("Gomez");
        
        Usuario usuarioGuardado = crearUsuario(1, "Juan", TipoUsuario.ESTUDIANTE, true);
        usuarioGuardado.setEmailInstitucional("jperezgomez@example.com");
        
        when(usuarioRepository.save(usuario)).thenReturn(usuarioGuardado);
        
        // Ejecución
        Usuario resultado = service.crearUsuario(usuario);
        
        // Verificación
        assertThat(resultado.getIdUsuario()).isEqualTo(1);
        assertThat(resultado.isActivo()).isTrue();
        assertThat(resultado.getEmailInstitucional()).isNotNull();
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void testAsignarRolAUsuario() {
        // Configuración
        Usuario usuario = crearUsuario(1, "Juan", TipoUsuario.ESTUDIANTE, true);
        Rol rol = crearRol(1L, "ROL_ESTUDIANTE");
        
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        
        // Ejecución
        Usuario resultado = service.asignarRolAUsuario(1, 1L);
        
        // Verificación
        assertThat(resultado.getRol()).isEqualTo(rol);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void testVincularCurso() {
        // Configuración
        int idUsuario = 1;
        Long idCurso = 100L;
        
        AlumnoCursoAceptado vinculacion = new AlumnoCursoAceptado();
        vinculacion.setIdUsuario(idUsuario);
        vinculacion.setIdCurso(idCurso);
        
        when(cursoAceptadoService.registrarAlumnoCurso(idUsuario, idCurso))
            .thenReturn(vinculacion);
        
        // Ejecución
        service.vincularCurso(idUsuario, idCurso);
        
        // Verificación
        verify(cursoAceptadoService).registrarAlumnoCurso(idUsuario, idCurso);
    }

    @Test
    void testCambiarEstadoUsuario() {
        // Configuración
        Usuario usuario = crearUsuario(1, "Juan", TipoUsuario.ESTUDIANTE, true);
        
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        
        Usuario usuarioDesactivado = crearUsuario(1, "Juan", TipoUsuario.ESTUDIANTE, false);
        when(usuarioRepository.save(usuario)).thenReturn(usuarioDesactivado);
        
        // Ejecución
        Usuario resultado = service.cambiarEstadoUsuario(1);
        
        // Verificación
        assertThat(resultado.isActivo()).isFalse();
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void testListarUsuariosPorId_Existente() {
        // Configuración
        Usuario usuario = crearUsuario(1, "Juan", TipoUsuario.ESTUDIANTE, true);
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        
        // Ejecución
        Usuario resultado = service.listarUsuariosPorId(1);
        
        // Verificación
        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdUsuario()).isEqualTo(1);
        verify(usuarioRepository).findById(1);
    }

    @Test
    void testListarUsuariosPorId_NoExistente() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.empty());
        
        Usuario resultado = service.listarUsuariosPorId(1);
        
        assertThat(resultado).isNull();
        verify(usuarioRepository).findById(1);
    }

    @Test
    void testActualizarUsuario() {
        // Configuración
        Usuario existente = crearUsuario(1, "Juan", TipoUsuario.ESTUDIANTE, true);
        existente.setEmailInstitucional("jperez@example.com");
        
        Usuario actualizado = crearUsuario(0, "Juan Carlos", TipoUsuario.PROFESOR, true);
        actualizado.setApellidoPUsuario("Gonzalez");
        
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(existente)).thenReturn(existente);
        
        // Ejecución
        Usuario resultado = service.actualizarUsuario(1, actualizado);
        
        // Verificación
        assertThat(resultado.getNombreUsuario()).isEqualTo("Juan Carlos");
        assertThat(resultado.getTipoUsuario()).isEqualTo(TipoUsuario.PROFESOR);
        assertThat(resultado.getApellidoPUsuario()).isEqualTo("Gonzalez");
        assertThat(resultado.getEmailInstitucional()).isNotNull(); // Debe mantenerse o regenerarse
        verify(usuarioRepository).save(existente);
    }
}