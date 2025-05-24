package com.Microservicio.GestionDeUsuariosYRoles.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Microservicio.GestionDeUsuariosYRoles.model.Usuario;

@Service
public class UsuarioService {
    
    @Autowired
    pritave UsuarioRepository usuarioRepository;

    public ResponseEntity<Usuario> leerUsuarios()
    {
        return new ResponseEntity<>(usuarioRepository.findAll(), HttpStatus.OK);
    }
}
