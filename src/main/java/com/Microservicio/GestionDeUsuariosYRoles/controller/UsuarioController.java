package com.Microservicio.GestionDeUsuariosYRoles.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Microservicio.GestionDeUsuariosYRoles.model.Usuario;
import com.Microservicio.GestionDeUsuariosYRoles.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> getUsuarios() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        if (usuarios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(usuarioService.listarUsuarios(), HttpStatus.OK);
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable int idUsuario) {
        Usuario usuario = usuarioService.listarUsuariosPorId(idUsuario);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Devuelve 404
        }
        return new ResponseEntity<>(usuario, HttpStatus.OK); // Devuelve 200 si existe
    }

    @GetMapping("/profesores")
    public ResponseEntity<List<Usuario>> getProfesores() {
        return ResponseEntity.ok(usuarioService.listarProfesores());
    }

    @GetMapping("/administradores")
    public ResponseEntity<List<Usuario>> getAdministradores() {
        return ResponseEntity.ok(usuarioService.listarAdministradores());
    }

    @GetMapping("/estudiantes")
    public ResponseEntity<List<Usuario>> getEstudiantes() {
        return ResponseEntity.ok(usuarioService.listarEstudiantes());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Usuario>> getUsuariosActivos() {
        return ResponseEntity.ok(usuarioService.listarUsuariosActivos());
    }

    @GetMapping("/inactivos")
    public ResponseEntity<List<Usuario>> getUsuariosInactivos() {
        return ResponseEntity.ok(usuarioService.listarUsuariosInactivos());
    }

    @PostMapping
    public ResponseEntity<Usuario> postUsuario(@RequestBody Usuario usuario) {
        return new ResponseEntity<>(usuarioService.crearUsuario(usuario), HttpStatus.OK);
    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity<Usuario> actualizarUsuario(
            @PathVariable int idUsuario,
            @RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(idUsuario, usuario));
    }

    @PutMapping("/{idUsuario}/estado")
    public ResponseEntity<Usuario> cambiarEstadoUsuario(@PathVariable int idUsuario) {
        return ResponseEntity.ok(usuarioService.cambiarEstadoUsuario(idUsuario));
    }

    @PutMapping("/asignar_rol")
    public ResponseEntity<Usuario> asignarRol(
            @RequestBody Map<String, Long> request) {

        return ResponseEntity.ok(
                usuarioService.asignarRolAUsuario(
                        request.get("idUsuario").intValue(),
                        request.get("idRol")));
    }

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable int idUsuario) {
        usuarioService.eliminarUsuario(idUsuario);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
