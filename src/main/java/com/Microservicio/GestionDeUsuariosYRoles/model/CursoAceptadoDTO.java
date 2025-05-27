package com.Microservicio.GestionDeUsuariosYRoles.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CursoAceptadoDTO {
    private Long cursoId;
    private String cursoNombre;
}
