package com.Microservicio.GestionDeUsuariosYRoles.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Microservicio.GestionDeUsuariosYRoles.model.Usuario;
import com.Microservicio.GestionDeUsuariosYRoles.repository.UsuarioRepository;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Mostrar a todos los usuarios
    public List<Usuario> listarUsuarios()
    {
        return usuarioRepository.findAll();
    }
}
