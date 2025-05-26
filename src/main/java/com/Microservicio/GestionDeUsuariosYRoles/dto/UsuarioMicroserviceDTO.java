package com.Microservicio.GestionDeUsuariosYRoles.dto;

import com.Microservicio.GestionDeUsuariosYRoles.model.TipoUsuario;
import lombok.Data;

@Data
public class UsuarioMicroserviceDTO {
    private int idUsuario;
    private String nombre;
    private TipoUsuario tipoUsuario;
}