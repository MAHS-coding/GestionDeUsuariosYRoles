package com.Microservicio.GestionDeUsuariosYRoles.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Microservicio.GestionDeUsuariosYRoles.service.AlumnoCursoAceptadoService;

@RestController
@RequestMapping("/alumnos-cursos")
public class AlumnoCursoAceptadoController {

    private final AlumnoCursoAceptadoService service;

    public AlumnoCursoAceptadoController(AlumnoCursoAceptadoService service) {
        this.service = service;
    }

    @GetMapping("/verificar")
    public ResponseEntity<Boolean> verificarAlumnoCurso(
            @RequestParam int idUsuario,
            @RequestParam Long idCurso) {
        boolean existe = service.existeAlumnoCurso(idUsuario, idCurso);
        return ResponseEntity.ok(existe);
    }

    @PostMapping("/registrar")
    public ResponseEntity<String> registrarAlumnoCurso(
            @RequestParam int idUsuario,
            @RequestParam Long idCurso) {
        try {
            service.registrarAlumnoCurso(idUsuario, idCurso);
            return ResponseEntity.ok("Inscripción aceptada registrada");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

