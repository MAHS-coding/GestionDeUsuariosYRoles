package com.Microservicio.GestionDeUsuariosYRoles.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUsuario;

    @Enumerated
    @Column(nullable = false)
    private TipoUsuario tipoUsuario;

    @Column(length = 150, nullable = false)
    private String nombreUsuario;

    @Column(length = 150, nullable = false)
    private String apellidoPUsuario;

    @Column(length = 150, nullable = false)
    private String apellidoMUsuario;

    @Column(length = 150, nullable = false)
    private String emailInstitucional;

    @Column(nullable = false)
    private boolean activo = true;

    // Email automatico
    public void generarEmailInstitucional() {
        String nombreFormateado = this.nombreUsuario.toLowerCase().replace(" ", "");
        String apellidoFormateado = this.apellidoPUsuario.toLowerCase().replace(" ", "");
        String dominio = switch(this.tipoUsuario) {
            case ADMINISTRADOR -> "@admin.duocuc.cl";
            case PROFESOR -> "@profesor.duocuc.cl";
            case ESTUDIANTE -> "@duocuc.cl";
        };
        this.emailInstitucional = nombreFormateado + apellidoFormateado + dominio;
    }
}
