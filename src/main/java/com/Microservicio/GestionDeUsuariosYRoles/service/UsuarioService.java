package com.Microservicio.GestionDeUsuariosYRoles.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Microservicio.GestionDeUsuariosYRoles.model.Rol;
import com.Microservicio.GestionDeUsuariosYRoles.model.TipoUsuario;
import com.Microservicio.GestionDeUsuariosYRoles.model.Usuario;
import com.Microservicio.GestionDeUsuariosYRoles.repository.RolRepository;
import com.Microservicio.GestionDeUsuariosYRoles.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private AlumnoCursoAceptadoService alumnoCursoAceptadoService;

    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    // Mostrar a todos los usuarios
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // Mostrar usuarios por id
    public Usuario listarUsuariosPorId(int idUsuario) {
        return usuarioRepository.findById(idUsuario).orElse(null); // Retorna null si no existe
    }

    // Mostrar a todos los profesores
    public List<Usuario> listarProfesores() {
        return usuarioRepository.findByTipoUsuario(TipoUsuario.PROFESOR);
    }

    // Obtener todos los administradores
    public List<Usuario> listarAdministradores() {
        return usuarioRepository.findByTipoUsuario(TipoUsuario.ADMINISTRADOR);
    }

    // Mostrar todos los estudiantes
    public List<Usuario> listarEstudiantes() {
        return usuarioRepository.findByTipoUsuario(TipoUsuario.ESTUDIANTE);
    }

    // Mostrar todos los usuarios activos
    public List<Usuario> listarUsuariosActivos() {
        return usuarioRepository.findByActivo(true);
    }

    // Mostrar todos los usuarios inactivos
    public List<Usuario> listarUsuariosInactivos() {
        return usuarioRepository.findByActivo(false);
    }

    // Crear un nuevo usuario
    public Usuario crearUsuario(Usuario usuario) {
        // Establecer valores por defecto
        usuario.setActivo(true);
        usuario.generarEmailInstitucional();

        return usuarioRepository.save(usuario);
    }

    // Actualizar un usuario existente
    public Usuario actualizarUsuario(int idUsuario, Usuario usuarioActualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));

        // Actualizar campos
        usuarioExistente.setTipoUsuario(usuarioActualizado.getTipoUsuario());
        usuarioExistente.setNombreUsuario(usuarioActualizado.getNombreUsuario());
        usuarioExistente.setApellidoPUsuario(usuarioActualizado.getApellidoPUsuario());
        usuarioExistente.setApellidoMUsuario(usuarioActualizado.getApellidoMUsuario());

        // Regenerar email si cambió algún dato relevante
        usuarioExistente.generarEmailInstitucional();

        return usuarioRepository.save(usuarioExistente);
    }

    public Usuario cambiarEstadoUsuario(int idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario con ID: " + idUsuario + " no encontrado."));

        usuario.setActivo(!usuario.isActivo());
        return usuarioRepository.save(usuario);
    }

    // Eliminar Usuario
    public void eliminarUsuario(int idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new RuntimeException("Usuario con ID" + idUsuario + " no encontrado");
        }
        usuarioRepository.deleteById(idUsuario);
    }

    // Asignar rol a usuario
    public Usuario asignarRolAUsuario(Integer idUsuario, Long idRol) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        usuario.setRol(rol);
        return usuarioRepository.save(usuario);
    }

    // Test para vincular con curso
    public void vincularCurso(int idUsuario, Long idCurso) {
        // Usar el servicio para registrar el curso aceptado, que maneja la validación
        // de duplicados
        alumnoCursoAceptadoService.registrarAlumnoCurso(idUsuario, idCurso);
    }

}
