package com.Microservicio.GestionDeUsuariosYRoles.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Microservicio.GestionDeUsuariosYRoles.model.Rol;
import com.Microservicio.GestionDeUsuariosYRoles.service.RolService;

@RestController
@RequestMapping("/api/roles")
public class RolController {
    @Autowired
    private RolService rolService;

    @GetMapping
    public ResponseEntity<List<Rol>> listarRoles() {
        return ResponseEntity.ok(rolService.listarTodosLosRoles());
    }

    @GetMapping("/estado/{activo}")
    public ResponseEntity<List<Rol>> listarRolesPorEstado(@PathVariable boolean activo) {
        return ResponseEntity.ok(rolService.listarRolesPorEstado(activo));
    }

    @PostMapping
    public ResponseEntity<Rol> crearRol(@RequestBody Rol rol) {
        // Establecemos el estado activo por defecto
        rol.setActivo(true);
        return ResponseEntity.ok(rolService.crearRol(rol));
    }

    @PutMapping("/{idRol}/permisos")
    public ResponseEntity<Rol> editarPermisos(
            @PathVariable Long idRol,
            @RequestBody Set<String> permisos) {
        return ResponseEntity.ok(rolService.editarPermisos(idRol, permisos));
    }

    @GetMapping("/{idRol}/permisos")
    public ResponseEntity<Set<String>> consultarPermisos(@PathVariable Long idRol) {
        return ResponseEntity.ok(rolService.consultarPermisos(idRol));
    }

@PutMapping("/{idRol}/estado")
public ResponseEntity<Rol> cambiarEstadoRol(
        @PathVariable Long idRol,
        @RequestBody Map<String, Boolean> request) {
    Boolean estado = request.get("estado");
    if (estado == null) {
        throw new RuntimeException("El campo 'estado' es requerido en el cuerpo de la solicitud");
    }
    return ResponseEntity.ok(rolService.cambiarEstadoRol(idRol, estado));
}

    @DeleteMapping("/{idRol}")
    public ResponseEntity<Void> eliminarRol(@PathVariable Long idRol) {
        rolService.eliminarRol(idRol);
        return ResponseEntity.noContent().build();
    }
}
