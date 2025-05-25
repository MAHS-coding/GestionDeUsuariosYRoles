package com.Microservicio.GestionDeUsuariosYRoles.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Microservicio.GestionDeUsuariosYRoles.dto.UsuarioCreacionDTO;
import com.Microservicio.GestionDeUsuariosYRoles.model.Usuario;
import com.Microservicio.GestionDeUsuariosYRoles.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Mostrar a todos los usuarios
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // Mostrar usuarios por id
    public Usuario listarUsuariosPorId(int idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario con ID: " + idUsuario + " no encontrado."));
    }

    // Crear un nuevo usuario
    public Usuario crearUsuario(UsuarioCreacionDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setTipoUsuario(usuarioDTO.getTipoUsuario());
        usuario.setNombreUsuario(usuarioDTO.getNombreUsuario());
        usuario.setApellidoPUsuario(usuarioDTO.getApellidoPUsuario());
        usuario.setApellidoMUsuario(usuarioDTO.getApellidoMUsuario());
        usuario.setActivo(true);
        usuario.generarEmailInstitucional();
        
        return usuarioRepository.save(usuario);
    }

    // Actualizar un usuario existente
    public Usuario actualizarUsuario(int id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        // Actualizar solo los campos permitidos
        usuarioExistente.setNombreUsuario(usuarioActualizado.getNombreUsuario());
        usuarioExistente.setApellidoPUsuario(usuarioActualizado.getApellidoPUsuario());
        usuarioExistente.setApellidoMUsuario(usuarioActualizado.getApellidoMUsuario());

        // Email actualizado 
        usuarioExistente.generarEmailInstitucional();

        return usuarioRepository.save(usuarioExistente);
    }

    // Eliminar Usuario
    public void eliminarUsuario(int idUsuario)
    {
        if(!usuarioRepository.existsById(idUsuario))
        {
            throw new RuntimeException("Usuario con ID" + idUsuario + " no encontrado");
        }
        usuarioRepository.deleteById(idUsuario);
    }

}
