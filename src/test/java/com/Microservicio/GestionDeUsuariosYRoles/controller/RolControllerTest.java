package com.Microservicio.GestionDeUsuariosYRoles.controller;

import com.Microservicio.GestionDeUsuariosYRoles.model.Rol;
import com.Microservicio.GestionDeUsuariosYRoles.service.RolService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RolController.class)
class RolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RolService rolService;

    @Test
    void testListarRoles() throws Exception {
        Rol rol = new Rol(1L, "ADMIN", Set.of("permiso1", "permiso2"), true);
        Mockito.when(rolService.listarTodosLosRoles()).thenReturn(List.of(rol));

        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("ADMIN"))
                .andExpect(jsonPath("$[0].activo").value(true));
    }

    @Test
    void testListarRolesPorEstado() throws Exception {
        Rol rol = new Rol(1L, "ADMIN", Set.of("permiso1"), true);
        Mockito.when(rolService.listarRolesPorEstado(true)).thenReturn(List.of(rol));

        mockMvc.perform(get("/api/roles/estado/true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("ADMIN"));
    }

    @Test
    void testCrearRol() throws Exception {
        Rol rol = new Rol(1L, "ADMIN", Set.of("permiso1"), true);
        Mockito.when(rolService.crearRol(Mockito.any(Rol.class))).thenReturn(rol);

        mockMvc.perform(post("/api/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rol)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("ADMIN"));
    }

    @Test
    void testEditarPermisos() throws Exception {
        Set<String> permisos = Set.of("permiso1", "permiso2");
        Rol rol = new Rol(1L, "ADMIN", permisos, true);
        Mockito.when(rolService.editarPermisos(1L, permisos)).thenReturn(rol);

        mockMvc.perform(put("/api/roles/1/permisos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(permisos)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.permisos.length()").value(2));
    }

    @Test
    void testConsultarPermisos() throws Exception {
        Set<String> permisos = Set.of("permiso1", "permiso2");
        Mockito.when(rolService.consultarPermisos(1L)).thenReturn(permisos);

        mockMvc.perform(get("/api/roles/1/permisos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testCambiarEstadoRol() throws Exception {
        Rol rol = new Rol(1L, "ADMIN", Collections.emptySet(), false);
        Mockito.when(rolService.cambiarEstadoRol(1L, false)).thenReturn(rol);

        mockMvc.perform(put("/api/roles/1/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"estado\":false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo").value(false));
    }

    @Test
    void testEliminarRol() throws Exception {
        mockMvc.perform(delete("/api/roles/1"))
                .andExpect(status().isNoContent());
    }
}