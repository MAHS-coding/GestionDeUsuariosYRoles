package com.Microservicio.GestionDeUsuariosYRoles.dto;

import com.Microservicio.GestionDeUsuariosYRoles.model.TipoUsuario;
import lombok.Data;

@Data
public class UsuarioPublicDTO {
    private int idUsuario;
    private String nombreUsuario;
    private String apellidoPUsuario;
    private String apellidoMUsuario;
    private String emailInstitucional;
    private TipoUsuario tipoUsuario;
    private boolean activo;
}