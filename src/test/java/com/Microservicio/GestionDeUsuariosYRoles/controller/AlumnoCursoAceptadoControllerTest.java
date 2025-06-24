package com.Microservicio.GestionDeUsuariosYRoles.controller;

import com.Microservicio.GestionDeUsuariosYRoles.service.AlumnoCursoAceptadoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlumnoCursoAceptadoController.class)
class AlumnoCursoAceptadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlumnoCursoAceptadoService service;

    @Test
    void testVerificarAlumnoCursoExistente() throws Exception {
        Mockito.when(service.existeAlumnoCurso(1, 1L)).thenReturn(true);

        mockMvc.perform(get("/alumnos-cursos/verificar")
                .param("idUsuario", "1")
                .param("idCurso", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testVerificarAlumnoCursoNoExistente() throws Exception {
        Mockito.when(service.existeAlumnoCurso(1, 1L)).thenReturn(false);

        mockMvc.perform(get("/alumnos-cursos/verificar")
                .param("idUsuario", "1")
                .param("idCurso", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void testRegistrarAlumnoCursoExitoso() throws Exception {
        mockMvc.perform(post("/alumnos-cursos/registrar")
                .param("idUsuario", "1")
                .param("idCurso", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Inscripción aceptada registrada"));
    }

    @Test
    void testRegistrarAlumnoCursoDuplicado() throws Exception {
        Mockito.doThrow(new RuntimeException("Usuario ya está inscrito en el curso"))
                .when(service).registrarAlumnoCurso(1, 1L);

        mockMvc.perform(post("/alumnos-cursos/registrar")
                .param("idUsuario", "1")
                .param("idCurso", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Usuario ya está inscrito en el curso"));
    }
}