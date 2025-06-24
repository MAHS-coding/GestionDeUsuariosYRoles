package com.Microservicio.GestionDeUsuariosYRoles.controller;

import com.Microservicio.GestionDeUsuariosYRoles.model.AlumnoCursoAceptado;
import com.Microservicio.GestionDeUsuariosYRoles.model.Rol;
import com.Microservicio.GestionDeUsuariosYRoles.model.TipoUsuario;
import com.Microservicio.GestionDeUsuariosYRoles.model.Usuario;
import com.Microservicio.GestionDeUsuariosYRoles.service.AlumnoCursoAceptadoService;
import com.Microservicio.GestionDeUsuariosYRoles.service.UsuarioService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private AlumnoCursoAceptadoService alumnoCursoAceptadoService;

    private Usuario crearUsuarioPrueba(int id, TipoUsuario tipo, boolean activo) {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(id);
        usuario.setTipoUsuario(tipo);
        usuario.setNombreUsuario("Nombre" + id);
        usuario.setApellidoPUsuario("ApellidoP" + id);
        usuario.setApellidoMUsuario("ApellidoM" + id);
        usuario.setActivo(activo);
        usuario.generarEmailInstitucional(); // Genera el email automáticamente
        
        // Opcional: asignar un rol si es necesario
        if (tipo == TipoUsuario.ADMINISTRADOR || tipo == TipoUsuario.PROFESOR) {
            Rol rol = new Rol();
            rol.setId(1L);
            rol.setNombre(tipo.name());
            usuario.setRol(rol);
        }
        
        return usuario;
    }

    @Test
    void testGetUsuarios() throws Exception {
        Usuario usuario = crearUsuarioPrueba(1, TipoUsuario.ESTUDIANTE, true);
        Mockito.when(usuarioService.listarUsuarios()).thenReturn(List.of(usuario));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idUsuario").value(1))
                .andExpect(jsonPath("$[0].tipoUsuario").value("ESTUDIANTE"))
                .andExpect(jsonPath("$[0].emailInstitucional").exists());
    }

    @Test
    void testGetUsuariosEmpty() throws Exception {
        Mockito.when(usuarioService.listarUsuarios()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testObtenerUsuarioPorIdExistente() throws Exception {
        Usuario usuario = crearUsuarioPrueba(1, TipoUsuario.ESTUDIANTE, true);
        Mockito.when(usuarioService.listarUsuariosPorId(1)).thenReturn(usuario);

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(1))
                .andExpect(jsonPath("$.emailInstitucional").exists());
    }

    @Test
    void testObtenerUsuarioPorIdNoExistente() throws Exception {
        Mockito.when(usuarioService.listarUsuariosPorId(1)).thenReturn(null);

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetProfesores() throws Exception {
        Usuario profesor = crearUsuarioPrueba(1, TipoUsuario.PROFESOR, true);
        Mockito.when(usuarioService.listarProfesores()).thenReturn(List.of(profesor));

        mockMvc.perform(get("/api/usuarios/profesores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipoUsuario").value("PROFESOR"))
                .andExpect(jsonPath("$[0].emailInstitucional").value(profesor.getEmailInstitucional()));
    }

    @Test
    void testGetAdministradores() throws Exception {
        Usuario admin = crearUsuarioPrueba(1, TipoUsuario.ADMINISTRADOR, true);
        Mockito.when(usuarioService.listarAdministradores()).thenReturn(List.of(admin));

        mockMvc.perform(get("/api/usuarios/administradores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipoUsuario").value("ADMINISTRADOR"))
                .andExpect(jsonPath("$[0].emailInstitucional").value(admin.getEmailInstitucional()));
    }

    @Test
    void testGetEstudiantes() throws Exception {
        Usuario estudiante = crearUsuarioPrueba(1, TipoUsuario.ESTUDIANTE, true);
        Mockito.when(usuarioService.listarEstudiantes()).thenReturn(List.of(estudiante));

        mockMvc.perform(get("/api/usuarios/estudiantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipoUsuario").value("ESTUDIANTE"))
                .andExpect(jsonPath("$[0].emailInstitucional").value(estudiante.getEmailInstitucional()));
    }

    @Test
    void testGetUsuariosActivos() throws Exception {
        Usuario usuario = crearUsuarioPrueba(1, TipoUsuario.ESTUDIANTE, true);
        Mockito.when(usuarioService.listarUsuariosActivos()).thenReturn(List.of(usuario));

        mockMvc.perform(get("/api/usuarios/activos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idUsuario").value(1))
                .andExpect(jsonPath("$[0].activo").value(true));
    }

    @Test
    void testGetUsuariosInactivos() throws Exception {
        Usuario usuario = crearUsuarioPrueba(1, TipoUsuario.ESTUDIANTE, false);
        Mockito.when(usuarioService.listarUsuariosInactivos()).thenReturn(List.of(usuario));

        mockMvc.perform(get("/api/usuarios/inactivos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].activo").value(false));
    }

    @Test
    void testPostUsuario() throws Exception {
        Usuario usuario = crearUsuarioPrueba(1, TipoUsuario.ESTUDIANTE, true);
        Mockito.when(usuarioService.crearUsuario(Mockito.any(Usuario.class))).thenReturn(usuario);

        // Crear usuario sin email (debería generarse automáticamente)
        Usuario usuarioRequest = new Usuario();
        usuarioRequest.setTipoUsuario(TipoUsuario.ESTUDIANTE);
        usuarioRequest.setNombreUsuario("Nombre1");
        usuarioRequest.setApellidoPUsuario("ApellidoP1");
        usuarioRequest.setApellidoMUsuario("ApellidoM1");

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(1))
                .andExpect(jsonPath("$.emailInstitucional").exists());
    }

    @Test
    void testActualizarUsuario() throws Exception {
        Usuario usuario = crearUsuarioPrueba(1, TipoUsuario.ESTUDIANTE, true);
        Mockito.when(usuarioService.actualizarUsuario(1, Mockito.any(Usuario.class))).thenReturn(usuario);

        Usuario usuarioUpdate = new Usuario();
        usuarioUpdate.setNombreUsuario("NombreActualizado");
        usuarioUpdate.setApellidoPUsuario("ApellidoPActualizado");
        usuarioUpdate.setApellidoMUsuario("ApellidoMActualizado");

        mockMvc.perform(put("/api/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(1))
                .andExpect(jsonPath("$.emailInstitucional").exists());
    }

    @Test
    void testCambiarEstadoUsuario() throws Exception {
        Usuario usuario = crearUsuarioPrueba(1, TipoUsuario.ESTUDIANTE, false);
        Mockito.when(usuarioService.cambiarEstadoUsuario(1)).thenReturn(usuario);

        mockMvc.perform(put("/api/usuarios/1/estado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo").value(false));
    }

    @Test
    void testAsignarRol() throws Exception {
        Usuario usuario = crearUsuarioPrueba(1, TipoUsuario.ESTUDIANTE, true);
        Rol rol = new Rol();
        rol.setId(1L);
        rol.setNombre("NUEVO_ROL");
        usuario.setRol(rol);
        
        Mockito.when(usuarioService.asignarRolAUsuario(1, 1L)).thenReturn(usuario);

        mockMvc.perform(put("/api/usuarios/asignar_rol")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"idUsuario\":1,\"idRol\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(1))
                .andExpect(jsonPath("$.rol").exists());
    }

    @Test
    void testEliminarUsuario() throws Exception {
        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testVincularCurso() throws Exception {
        mockMvc.perform(put("/api/usuarios/1/vincular-curso")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"cursoId\":1}"))
                .andExpect(status().isOk());
    }

    @Test
    void testObtenerCursosAceptados() throws Exception {
        AlumnoCursoAceptado curso = new AlumnoCursoAceptado();
        curso.setIdCurso(1L);
        curso.setIdUsuario(1);
        curso.setIdCurso(1L);
        
        Mockito.when(alumnoCursoAceptadoService.obtenerCursosPorUsuario(1)).thenReturn(List.of(curso));

        mockMvc.perform(get("/api/usuarios/1/cursos/aceptados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idUsuario").value(1));
    }
}