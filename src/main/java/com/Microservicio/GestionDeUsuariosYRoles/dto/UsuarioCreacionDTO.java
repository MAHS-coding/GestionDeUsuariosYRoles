package com.Microservicio.GestionDeUsuariosYRoles.dto;

import com.Microservicio.GestionDeUsuariosYRoles.model.TipoUsuario;

import lombok.Data;

@Data
public class UsuarioCreacionDTO {
    private TipoUsuario tipoUsuario;
    private String nombreUsuario;
    private String apellidoPUsuario;
    private String apellidoMUsuario;
}
