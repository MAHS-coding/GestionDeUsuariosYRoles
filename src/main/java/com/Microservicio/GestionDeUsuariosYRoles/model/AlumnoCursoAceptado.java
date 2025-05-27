package com.Microservicio.GestionDeUsuariosYRoles.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "alumnos_cursos_aceptados")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlumnoCursoAceptado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAceptados;

    private int idUsuario;

    private Long idCurso;

}
